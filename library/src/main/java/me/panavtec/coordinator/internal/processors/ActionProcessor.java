package me.panavtec.coordinator.internal.processors;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import me.panavtec.coordinator.internal.model.MappedAction;

public interface ActionProcessor<T extends MappedAction> {
  public abstract List<T> processActions(RoundEnvironment elements);

  public abstract void process(Element e);

  public abstract boolean isValidAction(Element e);
}
