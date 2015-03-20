package me.panavtec.coordinator.compiler.writters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import me.panavtec.coordinator.compiler.model.EnclosingCoordinator;
import me.panavtec.coordinator.compiler.model.MappedCompleteCoordinator;
import me.panavtec.coordinator.compiler.model.MappedCoordinatedAction;
import me.panavtec.coordinator.compiler.model.MappedCoordinator;

public class CoordinatorWriter {

  private static final String INNER_CLASS_SUFFIX = "$$CoordinatorInjector";
  private static final String TARGET = "target";

  public void writeCoordinators(Collection<EnclosingCoordinator> coordinators, Filer filer) {
    for (EnclosingCoordinator parent : coordinators) {
      write(filer, parent);
    }
  }

  public void write(Filer filer, EnclosingCoordinator parent) {
    try {
      writeInstantation(filer, parent);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeInstantation(Filer filer, EnclosingCoordinator parent) throws IOException {
    List<MappedCoordinator> coordinators = parent.getCoordinators();
    MethodSpec.Builder methodBuilder = createMetod(parent);
    for (MappedCoordinator coordinator : coordinators) {
      addNewCoordinatorStatement(methodBuilder, coordinator);
      addCompleteActions(methodBuilder, coordinator);
    }
    TypeSpec coordinateInjectorClass = createInjectClass(parent, methodBuilder.build());
    JavaFile.builder(parent.getPackageName(), coordinateInjectorClass)
        .addFileComment("Do not modify this file!")
        .build()
        .writeTo(filer);
  }

  private TypeSpec createInjectClass(EnclosingCoordinator parent, MethodSpec coordinateInject) {
    return TypeSpec.classBuilder(parent.getClassName() + INNER_CLASS_SUFFIX)
        .addModifiers(Modifier.PUBLIC)
        .addMethod(coordinateInject)
        .build();
  }

  private MethodSpec.Builder createMetod(EnclosingCoordinator parent) {
    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("coordinateInject");
    methodBuilder
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(void.class)
        .addParameter(ClassName.get(parent.getPackageName(), parent.getClassName()), TARGET,
            Modifier.FINAL);
    return methodBuilder;
  }

  private void addCompleteActions(MethodSpec.Builder methodBuilder, MappedCoordinator coordinator) {
    for (MappedCoordinatedAction action : coordinator.getTriggerActions()) {
      if (action.isMethod()) {
        methodBuilder.addStatement(TARGET + ".$L.doWhen($L, $L)",
            coordinator.getCoordinatorField(),
            action.getActionId(),
            createRunnable("$L", TARGET + "." + action.getFieldName() + "()")
        );
      } else {
        methodBuilder.addStatement(TARGET + ".$L.doWhen($L, target.$L)",
            coordinator.getCoordinatorField(),
            action.getActionId(),
            action.getFieldName()
        );
      }
    }
  }

  private void addNewCoordinatorStatement(MethodSpec.Builder methodBuilder,
      MappedCoordinator coordinator) {
    MappedCompleteCoordinator completeCoordinator = coordinator.getCompleteCoordinator();
    if (completeCoordinator.isMethod()) {
      methodBuilder.addStatement(
          TARGET + ".$L = new me.panavtec.coordinator.Coordinator($L, $L, $L)",
          coordinator.getCoordinatorField(),
          coordinator.getCoordinatorId(),
          createRunnable("$L.$L()", TARGET, completeCoordinator.getFieldName()),
          createArrayRepresentation(coordinator.getActions()));
    } else {
      methodBuilder.addStatement(
          TARGET + ".$L = new me.panavtec.coordinator.Coordinator($L, " + TARGET + ".$L, $L)",
          coordinator.getCoordinatorField(),
          coordinator.getCoordinatorId(),
          completeCoordinator.getFieldName(),
          createArrayRepresentation(coordinator.getActions()));
    }
  }

  private TypeSpec createRunnable(String format, String... parameters) {
    return TypeSpec.anonymousClassBuilder("")
        .addSuperinterface(ParameterizedTypeName.get(Runnable.class))
        .addMethod(MethodSpec.methodBuilder("run")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addStatement(format, parameters)
            .build())
        .build();
  }

  private String createArrayRepresentation(int[] actions) {
    String intArray = " ";
    for (int i : actions) {
      intArray += i + ",";
    }
    return intArray.substring(0, intArray.length() - 1);
  }
}
