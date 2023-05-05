package org.spell.spring;

import lombok.Getter;

@Getter
public enum Action {
  STARTER_ZIP("/starter.zip"),
  BUILD_GRADLE("/build.gradle"),
  POM_XML("/pom.xml");

  private final String value;

  Action(String value) {
    this.value = value;
  }

  public static Action getFromValue(String value) {
    if (STARTER_ZIP.getValue().equals(value)) {
      return STARTER_ZIP;
    }
    if (BUILD_GRADLE.getValue().equals(value)) {
      return BUILD_GRADLE;
    }
    if (POM_XML.getValue().equals(value)) {
      return POM_XML;
    }

    return STARTER_ZIP;
  }
}
