package org.spell.spring.client;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.spell.spring.Action;
import org.spell.spring.client.model.MetadataDto;
import org.springframework.http.client.ClientHttpRequestInterceptor;
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
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new HeaderRequestInterceptor("Accept", properties.getAcceptHeader()));
    restTemplate.setInterceptors(interceptors);
    MetadataDto response = restTemplate.getForObject(
        properties.getAddress() + properties.getMetadataPath(),
        MetadataDto.class);
    return response;
  }

  public byte[] download(Action action, String params) {
    return restTemplate.getForObject(
        properties.getAddress() + action.getValue() + params,
        byte[].class);
  }
}
