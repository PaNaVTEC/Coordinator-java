package me.panavtec.coordinator.internal.processors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import me.panavtec.coordinator.Coordinator;
import me.panavtec.coordinator.internal.model.CoordinatorParent;
import me.panavtec.coordinator.internal.model.MappedCoordinator;
import me.panavtec.coordinator.internal.model.MappedCoordinatorComplete;

public class CoordinatorWriter {

  private static final String INNER_CLASS_SUFFIX = "$$CoordinatorInjector";

  public void write(Filer filer, CoordinatorParent parent, MappedCoordinatorComplete complete) {
    try {
      writeCoordinatorsInstantion(filer, parent, complete);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeCoordinatorsInstantion(Filer buffer, CoordinatorParent parent,
      MappedCoordinatorComplete complete)
      throws IOException {
    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("coordinateInject");
    methodBuilder
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(void.class);

    addNewCoordinatorStatement(methodBuilder, parent, complete);

    TypeSpec coordinateInjectorClass =
        TypeSpec.classBuilder(parent.getClassName() + INNER_CLASS_SUFFIX)
            .addModifiers(Modifier.PUBLIC)
            //.addTypeVariable(TypeVariableName.get("T extends " + parent.getClassName()))
            .addMethod(methodBuilder.build())
            .build();

    JavaFile javaFile = JavaFile.builder(parent.getPackageName(), coordinateInjectorClass)
        .build();

    javaFile.writeTo(buffer);
  }

  private void addNewCoordinatorStatement(MethodSpec.Builder methodBuilder,
      CoordinatorParent parent, MappedCoordinatorComplete complete) {
    for (MappedCoordinator coordinator : parent.getCoordinators()) {
      methodBuilder
          .addParameter(ClassName.get(parent.getPackageName(), parent.getClassName()), "target")
          .addStatement("target.$L = new $T($L, $L, $L)",
              coordinator.getCoordinatorField(),
              Coordinator.class,
              coordinator.getCoordinatorId(),
              "target."+complete.getFieldName(),
              createArrayRepresentation(coordinator.getActions()));
    }
  }

  private String createArrayRepresentation(int[] actions) {
    String intArray = " ";
    for (int i : actions) {
      intArray += i +",";
    }
    return intArray.substring(0, intArray.length() -1);
  }
}
