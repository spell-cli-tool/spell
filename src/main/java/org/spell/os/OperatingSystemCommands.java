package org.spell.os;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;
import org.spell.common.FileManager;
import org.spell.common.ShellHelper;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("Operating System Commands")
@RequiredArgsConstructor
public class OperatingSystemCommands {

  private final ShellHelper shellHelper;
  private final FileManager fileManager;

  @ShellMethod(key = "pwd", value = "Show full path to the current directory")
  public String pwd() {
    return fileManager.getCurrentDirectoryPath();
  }

  @ShellMethod(key = "ls", value = "Show list of files and directories")
  public void ls() {
    File[] listOfFiles = fileManager.retrieveCurrentListOfFiles();

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
