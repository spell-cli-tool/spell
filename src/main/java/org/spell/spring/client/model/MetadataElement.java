package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class MetadataElement<T extends MetadataValue> {

  @JsonProperty("type")
  protected String type;

  @JsonProperty("default")
  protected String defaultValue;

  @JsonProperty("values")
  protected List<T> values;
}
