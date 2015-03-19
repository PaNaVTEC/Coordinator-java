package me.panavtec.coordinator.internal.model;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorParent {

  private String completeName;
  private String packageName;
  private String className;
  private String completedAction;
  private List<MappedCoordinator> coordinators = new ArrayList<>();

  public String getCompleteName() {
    return completeName;
  }

  public void setCompleteName(String completeName) {
    this.completeName = completeName;
  }

  public List<MappedCoordinator> getCoordinators() {
    return coordinators;
  }

  public void setCoordinators(List<MappedCoordinator> coordinators) {
    this.coordinators = coordinators;
  }

  @Override public boolean equals(Object obj) {
    return obj instanceof CoordinatorParent && ((CoordinatorParent) obj).getCompleteName()
        .equals(completeName);
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getCompletedAction() {
    return completedAction;
  }

  public void setCompletedAction(String completedAction) {
    this.completedAction = completedAction;
  }
}
