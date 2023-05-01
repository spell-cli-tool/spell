package org.spell.spring;

import lombok.RequiredArgsConstructor;
import org.spell.spring.client.InitializerClient;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent(value = "Initializer Commands")
@RequiredArgsConstructor
public class SpringInitializerCommands {


  private final InitializerClient client;

  @ShellMethod(key = "settings", value = "Shows Spring Initialzer settings details")
  public String settings() {
    return client.getSettings();
  }

  @ShellMethod(key = "metadata", value = "Provides Spring Initialzer metadata")
  public String metadata() {
    return client.getMetadata().getGroupId().getDefaultValue();
  }
}
