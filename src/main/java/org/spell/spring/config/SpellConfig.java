package org.spell.spring.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpellConfig {

  public static final String CONFIG_NAME = "spell-config.json";

  private String defaultGroup = "";
  private String defaultArtifact = "";

  private List<Template> templates = new ArrayList<>();
}
