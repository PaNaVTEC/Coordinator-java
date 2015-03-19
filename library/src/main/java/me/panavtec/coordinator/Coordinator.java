package me.panavtec.coordinator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import me.panavtec.coordinator.listeners.CompleteAction;
import me.panavtec.coordinator.listeners.CoordinatorComplete;

public class Coordinator {

  private final Set<Integer> actions = new HashSet<>();
  private final Set<Integer> completedActions = new HashSet<>();
  private final HashMap<Integer, CompleteAction> actionsCallbacks = new HashMap<>();
  private final CoordinatorComplete coordinatorComplete;

  public Coordinator(CoordinatorComplete coordinatorComplete, Integer... actions) {
    this.coordinatorComplete = coordinatorComplete;
    Collections.addAll(this.actions, actions);
    checkArguments();
  }

  public void completeAction(Integer action) {
    if (actions.contains(action)) {
      invokeActionCallback(action);
      completedActions.add(action);
      checkAllActionsComplete();
    }
  }

  private void invokeActionCallback(Integer action) {
    if (!completedActions.contains(action) && actionsCallbacks.containsKey(action)) {
      actionsCallbacks.get(action).onActionComplete();
    }
  }

  public void doWhen(Integer action, CompleteAction completeAction) {
    actionsCallbacks.put(action, completeAction);
  }

  public void reset() {
    completedActions.clear();
    actionsCallbacks.clear();
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
      coordinatorComplete.onCoordinatorComplete();
    }
  }
}