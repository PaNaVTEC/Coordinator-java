package me.panavtec.coordinator.internal.model;

import java.util.ArrayList;
import java.util.List;

public class EnclosingCoordinator {

  private String completeName;
  private String packageName;
  private String className;
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

  @Override public boolean equals(Object obj) {
    return obj instanceof EnclosingCoordinator && ((EnclosingCoordinator) obj).getCompleteName()
        .equals(completeName);
  }

}
