package me.panavtec.coordinator.internal.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import me.panavtec.coordinator.internal.model.MappedCompleteAction;
import me.panavtec.coordinator.internal.processors.tools.ElementTools;
import me.panavtec.coordinator.qualifiers.CoordinatedAction;

public class ActionCompleteProcessor implements ActionProcessor<MappedCompleteAction> {

  private List<MappedCompleteAction> completedActions = new ArrayList<>();
  private ElementTools elementTools = new ElementTools();

  @Override public List<MappedCompleteAction> processActions(RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CoordinatedAction.class);
    for (Element e : elements) {
      process(e);
    }
    return completedActions;
  }

  @Override public void process(Element e) {
    if (isValidAction(e)) {
      completedActions.add(createMappedCompleteAction(e));
    }
  }

  private MappedCompleteAction createMappedCompleteAction(Element e) {
    MappedCompleteAction action = new MappedCompleteAction();
    CoordinatedAction mappedAnnotation = annotationForElement(e);
    action.setActionId(mappedAnnotation.action());
    action.setCoordinatorId(mappedAnnotation.coordinatorId());
    action.setFieldName(elementTools.getFieldName(e));
    action.setParentName(elementTools.getElementParentCompleteClassName(e));
    System.out.println(action.toString());
    return action;
  }

  private CoordinatedAction annotationForElement(Element e) {
    return e.getAnnotation(CoordinatedAction.class);
  }

  @Override public boolean isValidAction(Element e) {
    return elementTools.isField(e) || elementTools.isMethod(e);
  }
}
