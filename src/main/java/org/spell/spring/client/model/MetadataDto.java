package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MetadataDto {

  @JsonProperty("type")
  private TypeElement type;

  @JsonProperty("language")
  private LanguageElement language;

  @JsonProperty("bootVersion")
  private BootVersionElement bootVersion;

  @JsonProperty("groupId")
  private TextElement groupId;

  @JsonProperty("artifactId")
  private TextElement artifactId;

  @JsonProperty("version")
  private TextElement version;

  @JsonProperty("name")
  private TextElement name;

  @JsonProperty("description")
  private TextElement description;

  @JsonProperty("packageName")
  private TextElement packageName;

  @JsonProperty("packaging")
  private PackagingElement packaging;

  @JsonProperty("javaVersion")
  private JavaVersionElement javaVersion;

  @JsonProperty("dependencies")
  private DependenciesElement dependencies;
}
