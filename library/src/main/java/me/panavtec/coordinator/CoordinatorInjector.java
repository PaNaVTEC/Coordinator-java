package me.panavtec.coordinator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CoordinatorInjector<T> {

  private static final String COORDINATE_INJECTOR_SUFFIX = "$$CoordinatorInjector";

  public Coordinator inject(T source) {
    try {
      Class<?> container = getInjectorContainerClass(source.getClass());
      Class<?> injector = Class.forName(container.getCanonicalName() + COORDINATE_INJECTOR_SUFFIX);
      Method inject = injector.getMethod("coordinateInject", container);
      return (Coordinator) inject.invoke(null, source);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Class<?> getInjectorContainerClass(Class<?> sourceClass) {
    try {
      Class.forName(sourceClass.getName() + COORDINATE_INJECTOR_SUFFIX);
      return Class.forName(sourceClass.getName());
    } catch (ClassNotFoundException e) {
      return getInjectorContainerClass(sourceClass.getSuperclass());
    } catch (Throwable e) {
      return null;
    }
  }
}
