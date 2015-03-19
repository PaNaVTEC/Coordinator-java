package me.panavtec.coordinator.internal.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import me.panavtec.coordinator.internal.model.MappedCoordinatorComplete;
import me.panavtec.coordinator.qualifiers.CoordinatorComplete;

public class CoordinatorCompleteProcessor {

  private List<MappedCoordinatorComplete> completedActions = new ArrayList<>();

  public List<MappedCoordinatorComplete> processCompleteActions(
      Set<? extends Element> annotatedWithAction) {
    completedActions.clear();
    for (Element e : annotatedWithAction) {
      processComplete(e);
    }
    return completedActions;
  }

  private void processComplete(Element e) {
    if (isValidCoordinator(e)) {
      CoordinatorComplete mappedAnnotation = annotationForElement(e);
      MappedCoordinatorComplete complete = new MappedCoordinatorComplete();
      complete.setCoordinatorId(mappedAnnotation.coordinatorId());
      complete.setFieldName(e.getSimpleName().toString());
      complete.setParentName(getElementParentCompleteClassName(e));
      completedActions.add(complete);
    }
  }

  private CoordinatorComplete annotationForElement(Element e) {
    return e.getAnnotation(CoordinatorComplete.class);
  }

  private String getElementParentCompleteClassName(Element e) {
    return e.getEnclosingElement().toString();
  }

  private boolean isValidCoordinator(Element e) {
    return e.getKind() == ElementKind.FIELD || e.getKind() == ElementKind.METHOD;
  }
}
