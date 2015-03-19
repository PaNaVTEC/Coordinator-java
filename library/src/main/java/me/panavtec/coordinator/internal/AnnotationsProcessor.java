package me.panavtec.coordinator.internal;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7) @SupportedAnnotationTypes({
    "me.panavtec.coordinator.qualifiers.Action", "me.panavtec.coordinator.qualifiers.Actions",
})
public class AnnotationsProcessor extends AbstractProcessor {

  @Override public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {
    
    
    return false;
  }
}
