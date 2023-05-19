package org.spell.spring.constant;

import java.util.regex.Pattern;

public final class InitializerConstant {

  private InitializerConstant() {
  }

  public static final Pattern GROUP_PATTERN = Pattern.compile("[a-z0-9.-]+");
  public static final Pattern ARTIFACT_PATTERN = Pattern.compile("[a-z0-9-]+");

  public static final Pattern DEPENDENCIES_PATTERN = Pattern.compile("[a-z0-9,-]+");
}
