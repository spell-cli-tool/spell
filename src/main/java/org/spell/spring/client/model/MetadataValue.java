package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class MetadataValue {

  @JsonProperty("id")
  protected String id;

  @JsonProperty("name")
  protected String name;
}
