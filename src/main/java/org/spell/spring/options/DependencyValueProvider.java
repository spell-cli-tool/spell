package org.spell.spring.options;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.spell.spring.service.InitializerService;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class DependencyValueProvider implements ValueProvider {

  private final InitializerService service;

  @Override
  public List<CompletionProposal> complete(CompletionContext completionContext) {
    List<String> ids = service.retrieveDependencyIds();
    String currentValue = completionContext.currentWord();
    if (StringUtils.hasText(currentValue) && currentValue.contains(",")) {
      int index = currentValue.lastIndexOf(",");
      if (index > 0 && index <= currentValue.length() - 1) {
        final String prefix = currentValue.substring(0, index);
        String dependency = currentValue.substring(index + 1);
        List<String> dependencies = Arrays.stream(prefix.split(",")).toList();
        ids = ids.stream()
            .filter(id -> id.startsWith(dependency) && !dependencies.contains(id))
            .map(id -> prefix + "," + id)
            .collect(Collectors.toList());
      }
    }

    return ids.stream()
        .map(CompletionProposal::new)
        .collect(Collectors.toList());
  }
}
