package me.panavtec.coordinator.internal.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MappedCoordinator {

  private int coordinatorId;
  private int[] actions;
  private String coordinatorField;
  private List<MappedCompleteAction> completedActions = new ArrayList<>();
  private MappedCompleteCoordinator completeCoordinator;

  public int getCoordinatorId() {
    return coordinatorId;
  }

  public void setCoordinatorId(int coordinatorId) {
    this.coordinatorId = coordinatorId;
  }

  public int[] getActions() {
    return actions;
  }

  public void setActions(int[] actions) {
    this.actions = actions;
  }

  public String getCoordinatorField() {
    return coordinatorField;
  }

  public void setCoordinatorField(String coordinatorField) {
    this.coordinatorField = coordinatorField;
  }

  public List<MappedCompleteAction> getCompletedActions() {
    return completedActions;
  }

  public MappedCompleteCoordinator getCompleteCoordinator() {
    return completeCoordinator;
  }

  public void setCompleteCoordinator(MappedCompleteCoordinator completeCoordinator) {
    this.completeCoordinator = completeCoordinator;
  }

  @Override public String toString() {
    return "MappedCoordinator{" +
        "coordinatorId=" + coordinatorId +
        ", actions=" + Arrays.toString(actions) +
        ", coordinatorField='" + coordinatorField + '\'' +
        ", completedActions=" + completedActions +
        ", completeCoordinator=" + completeCoordinator +
        '}';
  }
}
