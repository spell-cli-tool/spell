package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DependenciesValue extends MetadataValue {

  @JsonProperty("description")
  private String description;

  @JsonProperty("versionRange")
  private String versionRange;

  @JsonProperty("_links")
  private Links links;
}
