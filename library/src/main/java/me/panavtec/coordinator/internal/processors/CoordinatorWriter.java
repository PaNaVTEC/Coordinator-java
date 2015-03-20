package me.panavtec.coordinator.internal.processors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import me.panavtec.coordinator.Coordinator;
import me.panavtec.coordinator.internal.model.EnclosingCoordinator;
import me.panavtec.coordinator.internal.model.MappedCompleteAction;
import me.panavtec.coordinator.internal.model.MappedCompleteCoordinator;
import me.panavtec.coordinator.internal.model.MappedCoordinator;

public class CoordinatorWriter {

  private static final String INNER_CLASS_SUFFIX = "$$CoordinatorInjector";

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
        .addParameter(ClassName.get(parent.getPackageName(), parent.getClassName()), "target");
    return methodBuilder;
  }

  private void addCompleteActions(MethodSpec.Builder methodBuilder, MappedCoordinator coordinator) {
    for (MappedCompleteAction action : coordinator.getCompletedActions()) {
      if (action.isMethod()) {
        methodBuilder.addCode(""
            + "target."+coordinator.getCoordinatorField()+".doWhen("+action.getActionId()+", \n"
            + "new Runnable() {\n"
            + "\t@Override public void run() {\n"
            + "\t\ttarget."+action.getFieldName()+ "();\n"
            + "\t}\n"
            + "});\n");
      } else {
        methodBuilder.addStatement("target.$L.doWhen($L, target.$L)",
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
      methodBuilder.addCode(""
          + "target."+coordinator.getCoordinatorField()+ " = new Coordinator("+coordinator.getCoordinatorId()+", " +"\n"
          + "\tnew Runnable() {\n"
          + "\t\t@Override public void run() {\n"
          + "\t\t\ttarget."+completeCoordinator.getFieldName()+ "();\n"
          + "\t\t}\n"
          + "\t}, " + createArrayRepresentation(coordinator.getActions()) + ");\n");
    } else {
      methodBuilder.addStatement("target.$L = new $T($L, target.$L, $L)",
          coordinator.getCoordinatorField(),
          Coordinator.class,
          coordinator.getCoordinatorId(),
          completeCoordinator.getFieldName(),
          createArrayRepresentation(coordinator.getActions()));
    }

  }

  private String createArrayRepresentation(int[] actions) {
    String intArray = " ";
    for (int i : actions) {
      intArray += i + ",";
    }
    return intArray.substring(0, intArray.length() - 1);
  }
}
