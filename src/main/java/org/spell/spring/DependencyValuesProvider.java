package org.spell.spring;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.spell.spring.service.InitializerService;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DependencyValuesProvider implements ValueProvider {

  private final InitializerService service;

  @Override
  public List<CompletionProposal> complete(CompletionContext completionContext) {
    List<String> ids = service.retrieveDependenciesId();
    return ids.stream()
        .map(CompletionProposal::new)
        .collect(Collectors.toList());
  }
}
