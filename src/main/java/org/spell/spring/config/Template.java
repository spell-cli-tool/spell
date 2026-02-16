package org.spell.spring.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Template {

  private String name;
  private String type;
  private String language;
  private String bootVersion;
  private String group;
  private String artifact;
  private String folder;
  private String packaging;
  private String javaVersion;
  private String dependencies;
}
