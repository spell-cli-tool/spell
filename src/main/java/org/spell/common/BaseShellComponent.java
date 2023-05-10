package org.spell.common;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.shell.component.MultiItemSelector;
import org.springframework.shell.component.MultiItemSelector.MultiItemSelectorContext;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.SingleItemSelector.SingleItemSelectorContext;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.StringInput.StringInputContext;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;

public abstract class BaseShellComponent extends AbstractShellComponent {

  protected final ShellHelper shellHelper;

  public BaseShellComponent(ShellHelper shellHelper) {
    this.shellHelper = shellHelper;
  }

  protected String selectSingleItem(String name, List<SelectorItem<String>> items) {
    SingleItemSelector<String, SelectorItem<String>> component = new SingleItemSelector<>(getTerminal(),
        items, name, null);
    component.setResourceLoader(getResourceLoader());
    component.setTemplateExecutor(getTemplateExecutor());
    SingleItemSelectorContext<String, SelectorItem<String>> context = component
        .run(SingleItemSelectorContext.empty());
    return context.getResultItem().flatMap(si -> Optional.ofNullable(si.getItem())).get();
  }

  protected String setInput(String name, String defaultValue) {
    return setInput(name, defaultValue, null);
  }

  protected String setInput(String name, String defaultValue, Pattern pattern) {
    String result = "";
    boolean repeat;
    do {
      repeat = false;
      StringInput component = new StringInput(getTerminal(), name, defaultValue);
      component.setResourceLoader(getResourceLoader());
      component.setTemplateExecutor(getTemplateExecutor());
      StringInputContext context = component.run(StringInputContext.empty());

      result = context.getResultValue();

      if (pattern != null && !pattern.matcher(result).matches()) {
        shellHelper.printWarning(String.format("The value for '%s' must match the pattern %s",
            name, pattern.pattern()));
        repeat = true;
      }

    } while (repeat);

    return result;
  }

  protected List<String> selectMultipleItems(String name, List<SelectorItem<String>> items) {
    MultiItemSelector<String, SelectorItem<String>> component = new MultiItemSelector<>(
        getTerminal(), items, name, null);
    component.setResourceLoader(getResourceLoader());
    component.setTemplateExecutor(getTemplateExecutor());
    MultiItemSelectorContext<String, SelectorItem<String>> context = component
        .run(MultiItemSelectorContext.empty());

    return context.getResultItems().stream()
        .map(si -> si.getItem())
        .collect(Collectors.toList());
  }
}
