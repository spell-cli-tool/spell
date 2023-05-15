package org.spell.builtin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Version;

@ShellComponent
public class VersionCommand implements Version.Command {

  @Value("${application.formatted-version}")
  private String version;

  @ShellMethod(key = "version", value = "Show version info", group = "Built-In Commands")
  public String showVersion() {
    return version;
  }
}
