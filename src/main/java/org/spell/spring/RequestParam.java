package org.spell.spring;

import java.util.NoSuchElementException;
import lombok.Getter;

@Getter
public enum RequestParam {

  TYPE("type", "Project type: Gradle or Maven"),
  LANGUAGE("language", "Language: Java, Kotlin, Groovy"),
  BOOT_VERSION("bootVersion", "Spring Boot version"),
  GROUP_ID("groupId", "Project metadata: group id. Example: com.example"),
  ARTIFACT_ID("artifactId", "Project metadata: artifact id. Example: demo"),
  NAME("name", "Project metadata: name. Example: demo"),
  BASE_DIR("baseDir", "Project metadata: baseDir. Example: demo"),
  DESCRIPTION("description", "Project metadata: description. Example: Demo project for Spring Boot"),
  PACKAGE_NAME("packageName", "Project metadata: packageName. Example: com.example.demo"),
  PACKAGING("packaging", "Project metadata: packaging: Jar or War"),
  JAVA_VERSION("javaVersion", "Project metadata: javaVersion"),
  DEPENDENCIES("dependencies", "Spring project dependencies");

  private final String value;
  private final String description;

  RequestParam(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public static RequestParam getFromValue(String value) {
    if (TYPE.getValue().equals(value)) {
      return TYPE;
    }
    if (LANGUAGE.getValue().equals(value)) {
      return LANGUAGE;
    }
    if (BOOT_VERSION.getValue().equals(value)) {
      return BOOT_VERSION;
    }
    if (GROUP_ID.getValue().equals(value)) {
      return GROUP_ID;
    }
    if (ARTIFACT_ID.getValue().equals(value)) {
      return ARTIFACT_ID;
    }
    if (NAME.getValue().equals(value)) {
      return NAME;
    }
    if (BASE_DIR.getValue().equals(value)) {
      return BASE_DIR;
    }
    if (DESCRIPTION.getValue().equals(value)) {
      return DESCRIPTION;
    }
    if (PACKAGE_NAME.getValue().equals(value)) {
      return PACKAGE_NAME;
    }
    if (PACKAGING.getValue().equals(value)) {
      return PACKAGING;
    }
    if (JAVA_VERSION.getValue().equals(value)) {
      return JAVA_VERSION;
    }
    if (DEPENDENCIES.getValue().equals(value)) {
      return DEPENDENCIES;
    }

    throw new NoSuchElementException(String.format("Param %s doesn't exist!", value));
  }

}
