package org.spell.common;

import jakarta.validation.constraints.NotBlank;
import java.io.File;
import org.springframework.stereotype.Service;

@Service
public class FileManager {

  private String currentDirectoryPath = System.getProperty("user.dir");

  public String getCurrentDirectoryPath() {
    return currentDirectoryPath;
  }

  public String getParentDirectoryPath() {
    return getParentDirectoryPath(currentDirectoryPath);
  }

  public String getParentDirectoryPath(@NotBlank(message = "path can't be blank") String path) {
    int index = path.lastIndexOf("/");

    if (index >= 0 && index == path.length() - 1) {
      path = path.substring(0, path.length() - 1);
      index = path.lastIndexOf("/");
    }

    if (index < 0) {
      return path;
    }

    return path.substring(0, index);
  }

  public File[] getCurrentListOfFiles() {
    File folder = new File(currentDirectoryPath);
    return folder.listFiles();
  }

  public boolean isCurrentDirectoryEmpty() {
    return isDirectoryEmpty(currentDirectoryPath);
  }

  public boolean isDirectoryEmpty(String path) {
    File file = new File(path);

    if (file.exists() && file.isDirectory()) {
      return file.listFiles() == null || file.listFiles().length == 0;
    }

    return false;
  }

  public boolean isDirectoryHasName(@NotBlank(message = "path can't be blank") String path,
    @NotBlank(message = "name can't be blank") String name) {
    int index = path.lastIndexOf("/");

    if (index >= 0 && index == path.length() - 1) {
      path = path.substring(0, path.length() - 1);
      index = path.lastIndexOf("/");
    }

    if (index < 0) {
      return false;
    }

    String directoryName = path.substring(index + 1);

    return directoryName.equals(name);
  }

  public String findUniqueDirectoryName(String name) {
    File[] files = getCurrentListOfFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() && file.getName().equals(name)
            && file.listFiles() != null && file.listFiles().length > 0) {
          name = findUniqueDirectoryName(name, files);
          break;
        }
      }
    }
    return name;
  }

  private String findUniqueDirectoryName(String name, File[] files) {
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() && file.getName().equals(name)) {
          if (file.listFiles() != null) {
            name = findUniqueDirectoryName(name + "(1)", files, 2);
          }
          break;
        }
      }
    }

    return name;
  }

  private String findUniqueDirectoryName(String name, File[] files, int nextIndex) {
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() && file.getName().equals(name)) {
          if (file.listFiles() != null) {
            name = findUniqueDirectoryName(
                name.replace(String.format("(%s)", nextIndex - 1), "(" + nextIndex + ")"),
                files, nextIndex + 1);
          }
          break;
        }
      }
    }

    return name;
  }
}
