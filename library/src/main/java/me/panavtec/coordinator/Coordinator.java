package me.panavtec.coordinator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Coordinator {

  private final int coordinatorId;
  private final Set<Integer> actions = new HashSet<>();
  private final Set<Integer> completedActions = new HashSet<>();
  private final HashMap<Integer, Runnable> actionsCallbacks = new HashMap<>();
  private final Runnable coordinatorComplete;

  public static <T> Coordinator inject(T source) {
    try {
      Class<?> injector = Class.forName(source.getClass().getName() + "$$CoordinatorInjector");
      Method inject = injector.getMethod("coordinateInject", source.getClass());
      return (Coordinator) inject.invoke(null, source);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Coordinator(int coordinatorId, Runnable coordinatorComplete, Integer... actions) {
    this.coordinatorId = coordinatorId;
    this.coordinatorComplete = coordinatorComplete;
    Collections.addAll(this.actions, actions);
    checkArguments();
  }

  public Coordinator(Runnable coordinatorComplete, Integer... actions) {
    this(Integer.MAX_VALUE, coordinatorComplete, actions);
  }

  public void completeAction(Integer action) {
    if (actions.contains(action)) {
      invokeActionCallback(action);
      completedActions.add(action);
      checkAllActionsComplete();
    }
  }

  public void doWhen(Integer action, Runnable completeAction) {
    actionsCallbacks.put(action, completeAction);
  }

  public void reset() {
    completedActions.clear();
  }

  public int getCoordinatorId() {
    return coordinatorId;
  }

  private void invokeActionCallback(Integer action) {
    if (!completedActions.contains(action) && actionsCallbacks.containsKey(action)) {
      actionsCallbacks.get(action).run();
    }
  }

  private void checkArguments() {
    if (coordinatorComplete == null) {
      throw new NullPointerException("Coordinator complete action must not be null");
    }
    if (actions.size() == 0) {
      throw new IllegalArgumentException("No actions configured");
    }
  }

  private void checkAllActionsComplete() {
    if (actions.size() == completedActions.size()) {
      coordinatorComplete.run();
    }
  }

}