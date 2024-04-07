package org.spell.spring.options;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.spell.spring.service.SpellConfigService;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateValueProvider implements ValueProvider {

  private final SpellConfigService service;

  @Override
  public List<CompletionProposal> complete(CompletionContext completionContext) {
    List<String> templates = service.retrieveTemplateNames();

    return templates.stream()
        .map(CompletionProposal::new)
        .collect(Collectors.toList());
  }
}
