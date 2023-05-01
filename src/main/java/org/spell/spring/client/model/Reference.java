package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Reference {

  @JsonProperty("href")
  private String href;

  @JsonProperty("templated")
  private Boolean templated;
}
