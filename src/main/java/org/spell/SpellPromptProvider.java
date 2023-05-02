package org.spell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class SpellPromptProvider implements PromptProvider {

  @Override
  public AttributedString getPrompt() {
    return new AttributedString("spell:>",
        AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
  }
}
