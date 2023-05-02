package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Links {

  @JsonProperty("guide")
  private Object guide;

  @JsonProperty("reference")
  private Object reference;
}
