package me.panavtec.coordinator.compiler;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import me.panavtec.coordinator.compiler.model.EnclosingCoordinator;
import me.panavtec.coordinator.compiler.processors.CoordinatorProcessor;
import me.panavtec.coordinator.compiler.writters.CoordinatorWriter;
import me.panavtec.coordinator.qualifiers.Actions;
import me.panavtec.coordinator.qualifiers.CoordinatedAction;
import me.panavtec.coordinator.qualifiers.CoordinatorComplete;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationsProcessor extends AbstractProcessor {

  private CoordinatorWriter coordinatorWriter = new CoordinatorWriter();
  private CoordinatorProcessor coordinatorProcessor = new CoordinatorProcessor();
  private boolean firstProcessing;

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    firstProcessing = true;
  }

  @Override public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {
    if (!firstProcessing) {
      return false;
    }
    firstProcessing = false;
    processAnnotations(roundEnv);
    return true;
  }

  private void processAnnotations(RoundEnvironment roundEnv) {
    System.out.println("Starting Coordinator Annotation processor.");
    Collection<EnclosingCoordinator> coordinators =
        coordinatorProcessor.processAnnotations(roundEnv);
    coordinatorWriter.writeCoordinators(coordinators, processingEnv.getFiler());
  }

  @Override public Set<String> getSupportedAnnotationTypes() {
    Set<String> supportTypes = new LinkedHashSet<>();
    supportTypes.add(Actions.class.getCanonicalName());
    supportTypes.add(CoordinatedAction.class.getCanonicalName());
    supportTypes.add(CoordinatorComplete.class.getCanonicalName());
    return supportTypes;
  }
}
