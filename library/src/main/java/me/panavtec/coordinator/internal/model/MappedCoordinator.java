package me.panavtec.coordinator.internal.model;

import java.util.ArrayList;
import java.util.List;

public class MappedCoordinator {

  private int coordinatorId;
  private int[] actions;
  private String coordinatorField;
  private List<MappedCoordinatedAction> triggerActions = new ArrayList<>();
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

  public List<MappedCoordinatedAction> getTriggerActions() {
    return triggerActions;
  }

  public MappedCompleteCoordinator getCompleteCoordinator() {
    return completeCoordinator;
  }

  public void setCompleteCoordinator(MappedCompleteCoordinator completeCoordinator) {
    this.completeCoordinator = completeCoordinator;
  }
}
