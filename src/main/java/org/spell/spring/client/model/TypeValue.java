package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TypeValue extends MetadataValue {

  @JsonProperty("description")
  private String description;

  @JsonProperty("action")
  private String action;
}
