package me.panavtec.coordinator.internal;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import me.panavtec.coordinator.internal.model.CoordinatorParent;
import me.panavtec.coordinator.internal.model.MappedCoordinatorComplete;
import me.panavtec.coordinator.internal.processors.CoordinatorCompleteProcessor;
import me.panavtec.coordinator.internal.processors.CoordinatorProcessor;
import me.panavtec.coordinator.internal.processors.CoordinatorWriter;
import me.panavtec.coordinator.qualifiers.Actions;
import me.panavtec.coordinator.qualifiers.CoordinatorComplete;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({
    "me.panavtec.coordinator.qualifiers.Actions",
    "me.panavtec.coordinator.qualifiers.CoordinatedAction",
    "me.panavtec.coordinator.qualifiers.CoordinatorComplete"
})
public class AnnotationsProcessor extends AbstractProcessor {

  private CoordinatorProcessor coordinatorProcessor = new CoordinatorProcessor();
  private CoordinatorWriter coordinatorWriter = new CoordinatorWriter();
  private CoordinatorCompleteProcessor completeProcessor = new CoordinatorCompleteProcessor();

  @Override public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {
    System.out.println("####################");
    System.out.println("####################");
    System.out.println("Executing processor");

    Messager messager = processingEnv.getMessager();

    Collection<CoordinatorParent> coordinators = coordinatorProcessor.
        processCoordinators(roundEnv.getElementsAnnotatedWith(Actions.class));
    List<MappedCoordinatorComplete> mappedCoordinatorCompletes =
        completeProcessor.processCompleteActions(
            roundEnv.getElementsAnnotatedWith(CoordinatorComplete.class));

    for (CoordinatorParent parent : coordinators) {
      for (MappedCoordinatorComplete complete : mappedCoordinatorCompletes) {
        if (complete.getParentName().equals(parent.getCompleteName())) {
          coordinatorWriter.write(processingEnv.getFiler(), parent, complete);
        }
      }
    }

    System.out.println("####################");
    System.out.println("####################");
    return true;
  }
}
