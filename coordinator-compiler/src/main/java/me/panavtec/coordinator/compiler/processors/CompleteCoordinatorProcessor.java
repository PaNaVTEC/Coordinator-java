package me.panavtec.coordinator.compiler.processors;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import me.panavtec.coordinator.compiler.model.MappedCompleteCoordinator;
import me.panavtec.coordinator.compiler.qualifiers.CoordinatorComplete;

public class CompleteCoordinatorProcessor
    extends AbstractProcessor<MappedCompleteCoordinator> {

  @Override public Set<? extends Element> getElements(RoundEnvironment roundEnv) {
    return roundEnv.getElementsAnnotatedWith(CoordinatorComplete.class);
  }

  @Override protected MappedCompleteCoordinator createMappedAction(Element e) {
    CoordinatorComplete mappedAnnotation = e.getAnnotation(CoordinatorComplete.class);
    MappedCompleteCoordinator complete = new MappedCompleteCoordinator();
    complete.setCoordinatorId(mappedAnnotation.coordinatorId());
    return complete;
  }

  @Override public boolean isValidAction(Element e) {
    return elementTools.isField(e) || elementTools.isMethod(e);
  }
}
