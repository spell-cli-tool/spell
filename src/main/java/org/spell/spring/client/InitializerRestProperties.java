package org.spell.spring.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "initializer")
public class InitializerRestProperties {

  private String address = "https://start.spring.io";
  private String metadataPath = "/metadata/client";
  private String acceptHeader = "application/vnd.initializr.v2.2+json";
}
