package org.spell.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

  public List<Guide> retrieveGuides() {
    if (guide == null) return null;

    List<Guide> result = new ArrayList<>();

    if (guide instanceof ArrayList<?>) {
      ArrayList<?> guides = (ArrayList<?>) guide;
      for (var gd : guides) {
        if (gd instanceof LinkedHashMap<?, ?>) {
          result.add(createGuide((LinkedHashMap<String, String>) gd));
        }
      }
    } else if (guide instanceof LinkedHashMap<?, ?>) {
      result.add(createGuide((LinkedHashMap<String, String>) guide));
    }

    return result;
  }

  private Guide createGuide(LinkedHashMap<String, String> map) {
    return new Guide(map.get("href"), map.get("title"));
  }

  public List<Reference> retrieveReferences() {
    if (reference == null) return null;

    List<Reference> result = new ArrayList<>();

    if (reference instanceof ArrayList<?>) {
      ArrayList<?> references = (ArrayList<?>) reference;
      for (var ref : references) {
        if (ref instanceof LinkedHashMap<?, ?>) {
          result.add(createReference((LinkedHashMap<String, String>) ref));
        }
      }
    } else if (reference instanceof LinkedHashMap<?, ?>) {
      result.add(createReference((LinkedHashMap<String, String>) reference));
    }

    return result;
  }

  private Reference createReference(LinkedHashMap<String, ?> map) {
    return new Reference((String) map.get("href"),
        map.get("templated") != null ? (Boolean) map.get("templated") : false);
  }
}
