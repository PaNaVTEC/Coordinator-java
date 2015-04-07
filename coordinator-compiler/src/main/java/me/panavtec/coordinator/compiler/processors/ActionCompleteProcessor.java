package me.panavtec.coordinator.compiler.processors;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import me.panavtec.coordinator.compiler.model.MappedCoordinatedAction;
import me.panavtec.coordinator.qualifiers.CoordinatedAction;

public class ActionCompleteProcessor extends AbstractProcessor<MappedCoordinatedAction> {

  @Override public Set<? extends Element> getElements(RoundEnvironment roundEnv) {
    return roundEnv.getElementsAnnotatedWith(CoordinatedAction.class);
  }

  @Override protected MappedCoordinatedAction createMappedAction(Element e) {
    MappedCoordinatedAction action = new MappedCoordinatedAction();
    CoordinatedAction mappedAnnotation = e.getAnnotation(CoordinatedAction.class);
    action.setActionId(mappedAnnotation.action());
    action.setCoordinatorId(mappedAnnotation.coordinatorId());
    return action;
  }

  @Override public boolean isValidAction(Element e) {
    return elementTools.isField(e) || elementTools.isMethod(e);
  }
}
