package me.panavtec.coordinator.internal.processors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import me.panavtec.coordinator.Coordinator;
import me.panavtec.coordinator.internal.model.CoordinatorParent;
import me.panavtec.coordinator.internal.model.MappedCoordinator;
import me.panavtec.coordinator.qualifiers.Actions;

public class CoordinatorProcessor {

  private Map<String, CoordinatorParent> coordinators = new HashMap<>();

  public Collection<CoordinatorParent> processCoordinators(
      Set<? extends Element> annotatedWithAction) {
    for (Element e : annotatedWithAction) {
      processCoordinator(e);
    }
    return coordinators.values();
  }

  private void processCoordinator(Element e) {
    if (isValidCoordinator(e)) {
      String parentClassName = getElementParentCompleteClassName(e);
      initializeParent(e);
      coordinators.get(parentClassName).getCoordinators().add(processActionsOfCoordinator(e));
    }
  }

  private String getElementParentCompleteClassName(Element e) {
    return e.getEnclosingElement().toString();
  }

  private String getElementPackagename(Element e) {
    String parentClassName = getElementParentCompleteClassName(e);
    return parentClassName.substring(0, parentClassName.lastIndexOf('.'));
  }

  private void initializeParent(Element e) {
    String parentClassName = getElementParentCompleteClassName(e);
    if (!coordinators.containsKey(parentClassName)) {
      coordinators.put(parentClassName, createParent(e));
    }
  }

  private CoordinatorParent createParent(Element e) {
    CoordinatorParent parent = new CoordinatorParent();
    parent.setCompleteName(getElementParentCompleteClassName(e));
    parent.setPackageName(getElementPackagename(e));
    parent.setClassName(getElementParentClassName(e));

    System.out.println("className : " + parent.getCompleteName() + ", package: " + parent.getPackageName());

    return parent;
  }

  private String getElementParentClassName(Element e) {
    String parentClassName = getElementParentCompleteClassName(e);
    return parentClassName.substring(parentClassName.lastIndexOf('.') + 1, parentClassName.length());
  }

  private boolean isValidCoordinator(Element e) {
    return isField(e) && isCoordinator(e);
  }

  private MappedCoordinator processActionsOfCoordinator(Element e) {
    String fieldName = e.getSimpleName().toString();
    Actions mappedAnnotation = annotationForElement(e);

    int coordinatorId = mappedAnnotation.coordinatorId();
    Element parent = e.getEnclosingElement();

    System.out.println("Mapped FieldName: "
        + fieldName
        + ", "
        + coordinatorId
        + ", parent: "
        + parent.toString());

    MappedCoordinator mappedCoordinator = new MappedCoordinator();
    mappedCoordinator.setActions(mappedAnnotation.value());
    mappedCoordinator.setCoordinatorId(mappedAnnotation.coordinatorId());
    mappedCoordinator.setCoordinatorField(fieldName);
    return mappedCoordinator;
  }

  private Actions annotationForElement(Element e) {
    return e.getAnnotation(Actions.class);
  }

  private boolean isCoordinator(Element e) {
    return e.asType().toString().equals(Coordinator.class.getCanonicalName());
  }

  private boolean isField(Element e) {
    return e.getKind() == ElementKind.FIELD;
  }
}
