package org.spell.spring.client;

import lombok.RequiredArgsConstructor;
import org.spell.spring.client.model.MetadataDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class InitializerClient {

  private final RestTemplate restTemplate = new RestTemplate();
  private final InitializerRestProperties properties;

  public String retrieveSettingsInfo() {
    return restTemplate.getForObject(properties.getAddress(), String.class);
  }

  public MetadataDto retrieveMetadata() {
    MetadataDto response = restTemplate.getForObject(
        properties.getAddress() + properties.getMetadataPath(),
        MetadataDto.class);
    return response;
  }

  public byte[] downloadProject(String request) {
    return restTemplate.getForObject(
        properties.getAddress() + request,
        byte[].class);
  }
}
