package me.panavtec.coordinator.internal;

import java.util.Collection;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import me.panavtec.coordinator.internal.model.EnclosingCoordinator;
import me.panavtec.coordinator.internal.processors.CoordinatorProcessor;
import me.panavtec.coordinator.internal.processors.CoordinatorWriter;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({
    "me.panavtec.coordinator.qualifiers.Actions",
    "me.panavtec.coordinator.qualifiers.CoordinatedAction",
    "me.panavtec.coordinator.qualifiers.CoordinatorComplete"
})
public class AnnotationsProcessor extends AbstractProcessor {

  private CoordinatorWriter coordinatorWriter = new CoordinatorWriter();
  private CoordinatorProcessor coordinatorProcessor = new CoordinatorProcessor();

  @Override public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {
    System.out.println("Starting Coordinator Annotation processor.");
    Collection<EnclosingCoordinator> coordinators =
        coordinatorProcessor.processAnnotations(roundEnv);
    coordinatorWriter.writeCoordinators(coordinators, processingEnv.getFiler());
    return true;
  }

}
