package me.panavtec.coordinator.internal.processors;

import java.util.Collection;
import java.util.List;
import javax.annotation.processing.RoundEnvironment;
import me.panavtec.coordinator.internal.model.EnclosingCoordinator;
import me.panavtec.coordinator.internal.model.MappedCompleteCoordinator;
import me.panavtec.coordinator.internal.model.MappedCoordinatedAction;
import me.panavtec.coordinator.internal.model.MappedCoordinator;

public class CoordinatorProcessor {

  private CompleteCoordinatorProcessor completeProcessor = new CompleteCoordinatorProcessor();
  private EnclosingCoordinatorProcessor coordinatorProcessor = new EnclosingCoordinatorProcessor();
  private ActionCompleteProcessor actionCompleteProcessor = new ActionCompleteProcessor();

  public Collection<EnclosingCoordinator> processAnnotations(RoundEnvironment roundEnv) {
    Collection<EnclosingCoordinator> enclosings = processCoordinators(roundEnv);
    List<MappedCompleteCoordinator> complete = processComplete(roundEnv);
    List<MappedCoordinatedAction> actions = processCoordinatedActions(roundEnv);

    for (EnclosingCoordinator enclosing : enclosings) {
      assignCompleteForEnclosing(enclosing, complete);
      assingActionForEnclosing(enclosing, actions);
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

  private void assingActionForEnclosing(EnclosingCoordinator enclosing,
      List<MappedCoordinatedAction> actions) {
    for (MappedCoordinatedAction action : actions) {
      if (action.getParentName().equals(enclosing.getCompleteName())) {
        List<MappedCoordinator> coordinators = enclosing.getCoordinators();
        for (MappedCoordinator coordinator : coordinators) {
          if (coordinator.getCoordinatorId() == action.getCoordinatorId()) {
            coordinator.getTriggerActions().add(action);
            break;
          }
        }
      }
    }
  }

  private void assignCompleteForEnclosing(EnclosingCoordinator enclosing,
      List<MappedCompleteCoordinator> completes) {
    for (MappedCompleteCoordinator complete : completes) {
      if (complete.getParentName().equals(enclosing.getCompleteName())) {
        List<MappedCoordinator> coordinators = enclosing.getCoordinators();
        for (MappedCoordinator coordinator : coordinators) {
          if (coordinator.getCoordinatorId() == complete.getCoordinatorId()) {
            coordinator.setCompleteCoordinator(complete);
            break;
          }
        }
      }
    }
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
