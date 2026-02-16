package org.spell.spring.constant;

import java.util.regex.Pattern;

public final class CommandConstant {

  private CommandConstant() {
  }

  public static final String GROUP_PATTERN_VALUE = "[a-z0-9.-]+";
  public static final Pattern GROUP_PATTERN = Pattern.compile(GROUP_PATTERN_VALUE);

  public static final String ARTIFACT_PATTERN_VALUE = "[a-z0-9.-]+";
  public static final Pattern ARTIFACT_PATTERN = Pattern.compile(ARTIFACT_PATTERN_VALUE);

  public static final String NAME_PATTERN_VALUE = "[a-zA-Z0-9.-_ ]+";
  public static final Pattern NAME_PATTERN = Pattern.compile(NAME_PATTERN_VALUE);

  public static final Pattern DEPENDENCIES_PATTERN = Pattern.compile("[a-z0-9,-]+");

  public static final String TEMPLATE_NAME_PARAM = "--name";
  public static final String SHORT_TEMPLATE_NAME_PARAM = "--n";
  public static final String TYPE_PARAM = "--type";
  public static final String SHORT_TYPE_PARAM = "-t";
  public static final String LANGUAGE_PARAM = "--language";
  public static final String SHORT_LANGUAGE_PARAM = "-l";
  public static final String BOOT_VERSION_PARAM = "--boot-version";
  public static final String SHORT_BOOT_VERSION_PARAM = "-b";
  public static final String GROUP_PARAM = "--group-id";
  public static final String SHORT_GROUP_PARAM = "-g";
  public static final String ARTIFACT_PARAM = "--artifact-id";
  public static final String SHORT_ARTIFACT_PARAM = "-a";
  public static final String FOLDER_PARAM = "--folder";
  public static final String SHORT_FOLDER_PARAM = "-f";
  public static final String PACKAGING_PARAM = "--packaging";
  public static final String SHORT_PACKAGING_PARAM = "-p";
  public static final String JAVA_VERSION_PARAM = "--java-version";
  public static final String SHORT_JAVA_VERSION_PARAM = "-j";
  public static final String DEPENDENCIES_PARAM = "--dependencies";
  public static final String SHORT_DEPENDENCIES_PARAM = "-d";
  public static final String TEMPLATE_PARAM = "--template";

  public static final String REPLACE_PARAM = "--replace";
  public static final String SHORT_REPLACE_PARAM = "-r";

  public static final String JSON_PARAM = "--json";
  public static final String SHORT_JSON_PARAM = "-js";
}
