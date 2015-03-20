package me.panavtec.coordinator.compiler.processors;

import java.util.Collection;
import java.util.List;
import javax.annotation.processing.RoundEnvironment;
import me.panavtec.coordinator.compiler.model.EnclosingCoordinator;
import me.panavtec.coordinator.compiler.model.MappedAction;
import me.panavtec.coordinator.compiler.model.MappedCompleteCoordinator;
import me.panavtec.coordinator.compiler.model.MappedCoordinatedAction;
import me.panavtec.coordinator.compiler.model.MappedCoordinator;

public class CoordinatorProcessor {

  private CompleteCoordinatorProcessor completeProcessor = new CompleteCoordinatorProcessor();
  private EnclosingCoordinatorProcessor coordinatorProcessor = new EnclosingCoordinatorProcessor();
  private ActionCompleteProcessor actionCompleteProcessor = new ActionCompleteProcessor();

  public Collection<EnclosingCoordinator> processAnnotations(RoundEnvironment roundEnv) {
    Collection<EnclosingCoordinator> enclosings = processCoordinators(roundEnv);
    List<MappedCompleteCoordinator> complete = processComplete(roundEnv);
    List<MappedCoordinatedAction> actions = processCoordinatedActions(roundEnv);

    for (EnclosingCoordinator enclosing : enclosings) {
      for (MappedCoordinator coordinator : enclosing.getCoordinators()) {
        assignAction(actions, enclosing, coordinator);
        assignCompleteAction(complete, enclosing, coordinator);
      }
      checkForCompleteErrors(enclosing);
    }

    return enclosings;
  }

  private void checkForCompleteErrors(EnclosingCoordinator enclosing) {
    List<MappedCoordinator> coordinators = enclosing.getCoordinators();
    for (MappedCoordinator coordinator : coordinators) {
      if (coordinator.getCompleteCoordinator() == null) {
        throw new RuntimeException("No complete action configured for coordinator: "
            + coordinator.getCoordinatorField()
            + " on class: "
            + enclosing.getCompleteName());
      }
    }
  }

  private void assignCompleteAction(List<MappedCompleteCoordinator> complete,
      EnclosingCoordinator enclosing, MappedCoordinator coordinator) {
    MappedCompleteCoordinator completeAction = findAction(enclosing, coordinator, complete);
    if (completeAction != null) {
      coordinator.setCompleteCoordinator(completeAction);
    }
  }

  private void assignAction(List<MappedCoordinatedAction> actions, EnclosingCoordinator enclosing,
      MappedCoordinator coordinator) {
    MappedCoordinatedAction action = findAction(enclosing, coordinator, actions);
    if (action != null) {
      coordinator.getTriggerActions().add(action);
    }
  }

  private <T extends MappedAction> T findAction(EnclosingCoordinator enclosing,
      MappedCoordinator coordinator, List<T> actions) {
    for (T action : actions) {
      if (action.getParentName().equals(enclosing.getCompleteName())) {
        if (coordinator.getCoordinatorId() == action.getCoordinatorId()) {
          return action;
        }
      }
    }
    return null;
  }

  private Collection<EnclosingCoordinator> processCoordinators(RoundEnvironment roundEnv) {
    return coordinatorProcessor.processCoordinators(roundEnv);
  }

  private List<MappedCompleteCoordinator> processComplete(RoundEnvironment roundEnv) {
    return completeProcessor.processActions(roundEnv);
  }

  private List<MappedCoordinatedAction> processCoordinatedActions(RoundEnvironment roundEnv) {
    return actionCompleteProcessor.processActions(roundEnv);
  }
}
