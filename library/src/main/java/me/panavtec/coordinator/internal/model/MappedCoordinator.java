package me.panavtec.coordinator.internal.model;

public class MappedCoordinator {

  private int coordinatorId;
  private int[] actions;
  private String coordinatorField;

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
}
