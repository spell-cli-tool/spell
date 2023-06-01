package org.spell.exception;

import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;
import org.spell.common.ShellHelper;
import org.springframework.shell.command.CommandExceptionResolver;
import org.springframework.shell.command.CommandHandlingResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomExceptionResolver implements CommandExceptionResolver {

  private final ShellHelper shellHelper;

  @Override
  public CommandHandlingResult resolve(Exception e) {
    if (e instanceof BaseShellException) {
      String styledErrorMessage = shellHelper.getStyledMessage(e.getMessage(),
          AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold());
      return CommandHandlingResult.of(styledErrorMessage + "\n", ((BaseShellException) e).getExitCode());
    }
    return CommandHandlingResult.of(e.getMessage(), 0);
  }
}
