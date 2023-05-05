package org.spell.config;

import org.jline.terminal.Terminal;
import org.spell.ShellHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ShellConfig {

  @Bean
  public ShellHelper shellHelper(@Lazy Terminal terminal) {
    return new ShellHelper(terminal);
  }
}
