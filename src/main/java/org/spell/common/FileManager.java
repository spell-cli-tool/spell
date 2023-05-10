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

  public File[] retrieveCurrentListOfFiles() {
    File folder = new File(currentDirectoryPath);
    return folder.listFiles();
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

  public String retrieveParentDirectoryPath(@NotBlank(message = "path can't be blank") String path) {
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
}
