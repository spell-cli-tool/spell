package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DependenciesGroup extends MetadataValue {

  @JsonProperty("values")
  private List<DependenciesValue> values;
}
