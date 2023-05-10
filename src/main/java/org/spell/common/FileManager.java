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

  public boolean isUniqueName(String name, boolean forDirectory) {
    File[] files = getCurrentListOfFiles();
    return isUniqueName(name, files, forDirectory);
  }

  public boolean isUniqueName(String name, File[] files, boolean forDirectory) {
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() == forDirectory && file.getName().equals(name)) {
          return false;
        }
      }
    }
    return true;
  }

  public String findUniqueDirectoryName(String name) {
    File[] files = getCurrentListOfFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() && file.getName().equals(name)
            && file.listFiles() != null && file.listFiles().length > 0) {
          name = findUniqueDirectoryName(name, files, "__%s");
          break;
        }
      }
    }
    return name;
  }

  public String findUniqueDirectoryName(String name, File[] files, String suffixTemplate) {
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() && file.getName().equals(name)) {
          name = findUniqueName(name + String.format(suffixTemplate, 1),
              files, true, suffixTemplate, 2);
          break;
        }
      }
    }

    return name;
  }

  public String findUniqueFileName(String name, String extention, String suffixTemplate) {
    File[] files = getCurrentListOfFiles();
    String fullName = String.format("%s.%s", name, extention);

    if (isUniqueName(fullName, files, false)) {
      return fullName;
    }

    name = name + String.format(suffixTemplate, 1);

    if (files != null) {
      for (File file : files) {
        if (!file.isDirectory() && file.getName().equals(name)) {
          name = findUniqueName(name, files, false, suffixTemplate, 2);
          break;
        }
      }
    }
    return name;
  }

  public String findUniqueName(String name, File[] files, boolean forDirectory,
      String suffixTemplate, int nextIndex) {
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory() == forDirectory && file.getName().equals(name)) {
          name = findUniqueName(
              name.replace(String.format(suffixTemplate, nextIndex - 1),
                  String.format(suffixTemplate, nextIndex)),
              files, forDirectory, suffixTemplate, nextIndex + 1);
          break;
        }
      }
    }

    return name;
  }
}
