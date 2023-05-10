package org.spell.common;

import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShellHelper {

  private final Terminal terminal;

  public String getStyledMessage(String message, AttributedStyle attributedStyle) {
    return (new AttributedStringBuilder()).append(message, attributedStyle).toAnsi();
  }

  public void printSuccess(String message) {
    print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
  }

  public void printInfo(String message) {
    print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN));
  }

  public void printWarning(String message) {
    print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
  }

  public void printError(String message) {
    print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
  }

  public void emptyLine() {
    print("");
  }

  public void print(String message) {
    print(message, null);
  }

  public void print(String message, AttributedStyle attributedStyle) {
    if (attributedStyle != null) {
      message = getStyledMessage(message, attributedStyle);
    }
    terminal.writer().println(message);
    terminal.flush();
  }
}
