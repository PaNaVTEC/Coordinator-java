package me.panavtec.coordinator.internal.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import me.panavtec.coordinator.internal.model.MappedCompleteCoordinator;
import me.panavtec.coordinator.internal.processors.tools.ElementTools;
import me.panavtec.coordinator.qualifiers.CoordinatorComplete;

public class CoordinatorCompleteProcessor implements ActionProcessor<MappedCompleteCoordinator> {

  private List<MappedCompleteCoordinator> completedActions = new ArrayList<>();
  private ElementTools elementTools = new ElementTools();

  @Override public List<MappedCompleteCoordinator> processActions(RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CoordinatorComplete.class);
    for (Element e : elements) {
      process(e);
    }
    return completedActions;
  }

  @Override public void process(Element e) {
    if (isValidAction(e)) {
      completedActions.add(createMappedCompleteCoordinator(e));
    }
  }

  private MappedCompleteCoordinator createMappedCompleteCoordinator(Element e) {
    CoordinatorComplete mappedAnnotation = annotationForElement(e);
    MappedCompleteCoordinator complete = new MappedCompleteCoordinator();
    complete.setCoordinatorId(mappedAnnotation.coordinatorId());
    complete.setFieldName(elementTools.getFieldName(e));
    complete.setParentName(elementTools.getElementParentCompleteClassName(e));
    System.out.println(complete.toString());
    return complete;
  }

  private CoordinatorComplete annotationForElement(Element e) {
    return e.getAnnotation(CoordinatorComplete.class);
  }

  @Override public boolean isValidAction(Element e) {
    return elementTools.isField(e) || elementTools.isMethod(e);
  }
}
