package me.panavtec.coordinator.internal.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import me.panavtec.coordinator.internal.model.MappedAction;
import me.panavtec.coordinator.internal.processors.tools.ElementTools;

public abstract class AbstractProcessor<T extends MappedAction> {

  protected ElementTools elementTools = new ElementTools();

  public List<T> processActions(RoundEnvironment roundEnv) {
    Set<? extends Element> elements = getElements(roundEnv);
    ArrayList<T> mappedActions = new ArrayList<>();
    for (Element e : elements) {
      if (isValidAction(e)) {
        mappedActions.add(process(e));
      }
    }
    return mappedActions;
  }

  public abstract Set<? extends Element> getElements(RoundEnvironment roundEnv);

  protected T process(Element e) {
    T action = createMappedAction(e);
    setElementFieldsToAction(action, e);
    return action;
  }

  protected abstract T createMappedAction(Element e);

  public abstract boolean isValidAction(Element e);

  public void setElementFieldsToAction(T action, Element e) {
    action.setFieldName(elementTools.getFieldName(e));
    action.setParentName(elementTools.getElementParentCompleteClassName(e));
    action.setMethod(elementTools.isMethod(e));
  }

}
