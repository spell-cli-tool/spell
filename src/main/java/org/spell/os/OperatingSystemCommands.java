package org.spell.os;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;
import org.spell.ShellHelper;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("Operating System Commands")
@RequiredArgsConstructor
public class OperatingSystemCommands {

  private final ShellHelper shellHelper;

  @ShellMethod(key = "pwd", value = "Show full path of the current directory")
  public String pwd() {
    return System.getProperty("user.dir");
  }

  @ShellMethod(key = "ls", value = "Show list of files and directories")
  public void ls() {
    File folder = new File(System.getProperty("user.dir"));
    File[] listOfFiles = folder.listFiles();

    if (listOfFiles != null && listOfFiles.length > 0) {
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          shellHelper.print(listOfFiles[i].getName(),
              AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN));
        } else if (listOfFiles[i].isDirectory()) {
          shellHelper.print(listOfFiles[i].getName() + "/",
              AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA));
        }
      }
    }
  }
}
