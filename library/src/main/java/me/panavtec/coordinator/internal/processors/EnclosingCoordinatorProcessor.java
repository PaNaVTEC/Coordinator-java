package me.panavtec.coordinator.internal.processors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import me.panavtec.coordinator.Coordinator;
import me.panavtec.coordinator.internal.model.EnclosingCoordinator;
import me.panavtec.coordinator.internal.model.MappedCoordinator;
import me.panavtec.coordinator.internal.processors.tools.ElementTools;
import me.panavtec.coordinator.qualifiers.Actions;

public class EnclosingCoordinatorProcessor {

  private Map<String, EnclosingCoordinator> coordinators = new HashMap<>();
  private ElementTools elementTools = new ElementTools();

  public Collection<EnclosingCoordinator> processCoordinators(RoundEnvironment roundEnv) {
    Set<? extends Element> actions = roundEnv.getElementsAnnotatedWith(Actions.class);
    for (Element e : actions) {
      processCoordinator(e);
    }
    return coordinators.values();
  }

  private void processCoordinator(Element e) {
    if (isValidCoordinator(e)) {
      String parentClassName = elementTools.getElementParentCompleteClassName(e);
      initializeParent(e);
      coordinators.get(parentClassName).getCoordinators().add(processActionsOfCoordinator(e));
    }
  }

  private void initializeParent(Element e) {
    String parentClassName = elementTools.getElementParentCompleteClassName(e);
    if (!coordinators.containsKey(parentClassName)) {
      coordinators.put(parentClassName, createParent(e));
    }
  }

  private EnclosingCoordinator createParent(Element e) {
    EnclosingCoordinator parent = new EnclosingCoordinator();
    parent.setCompleteName(elementTools.getElementParentCompleteClassName(e));
    parent.setPackageName(elementTools.getElementPackagename(e));
    parent.setClassName(elementTools.getElementParentClassName(e));

    System.out.println("className : " + parent.getCompleteName() + ", package: " + parent.getPackageName());

    return parent;
  }

  private boolean isValidCoordinator(Element e) {
    return elementTools.isField(e) && isCoordinator(e);
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
    System.out.println(mappedCoordinator.toString());
    return mappedCoordinator;
  }

  private Actions annotationForElement(Element e) {
    return e.getAnnotation(Actions.class);
  }

  private boolean isCoordinator(Element e) {
    return elementTools.getElementType(e).equals(Coordinator.class.getCanonicalName());
  }
}
