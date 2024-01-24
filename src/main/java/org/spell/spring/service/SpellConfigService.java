package org.spell.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.spell.spring.config.SpellConfig;
import org.spell.spring.config.Template;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class SpellConfigService {

  private SpellConfig config;
  private Path configPath;

  private ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    configPath = getConfigPath(SpellConfig.CONFIG_NAME);
    createSpellConfigIfNeeded(configPath);
    try {
      config = objectMapper.readValue(configPath.toFile(), SpellConfig.class);
    } catch (Exception e) {
      config = new SpellConfig();
    }
  }

  public String retrievePath() {
    return configPath.toString();
  }

  public boolean isTemplateExist(String name) {
    if (!CollectionUtils.isEmpty(config.getTemplates())) {
      Optional<Template> existedTemplate = config.getTemplates()
          .stream()
          .filter(t -> t.getName().equalsIgnoreCase(name))
          .findFirst();
      return existedTemplate.isPresent();
    }
    return false;
  }

  public void createOrReplaceTemplate(Template template) {
    if (!CollectionUtils.isEmpty(config.getTemplates())) {
      var existedTemplates = config.getTemplates()
          .stream()
          .filter(t -> t.getName().equalsIgnoreCase(template.getName()))
          .toList();
      if (!CollectionUtils.isEmpty(existedTemplates)) {
        config.getTemplates().removeAll(existedTemplates);
      }
    }

    if (CollectionUtils.isEmpty(config.getTemplates())) {
      config.setTemplates(new ArrayList<>(List.of(template)));
    } else {
      config.getTemplates().add(template);
    }

    save();
  }

  public boolean deleteTemplate(String name) {
    if (name == null || "".equals(name)) {
      return false;
    }

    if (!CollectionUtils.isEmpty(config.getTemplates())) {
      Optional<Template> template = config.getTemplates()
          .stream()
          .filter(t -> t.getName().equalsIgnoreCase(name))
          .findFirst();
      if (template.isPresent()) {
        config.getTemplates().remove(template.get());
        save();
        return true;
      }
    }

    return false;
  }

  public SpellConfig retrieve() {
    return config;
  }

  public String retrieveAsString() {
    String result = "";
    try {
      result = objectMapper.writeValueAsString(config);
    } catch (JsonProcessingException e) {
      System.out.println(e.getMessage());
      result = "{}";
    }
    return result;
  }

  public void setDefaultGroup(String defaultGroup) {
    config.setDefaultGroup(defaultGroup);
    save();
  }

  public void setDefaultArtifact(String defaultArtifact) {
    config.setDefaultArtifact(defaultArtifact);
    save();
  }

  public void save() {
    saveConfigToFile(config, configPath);
  }

  private Path getConfigPath(String fileName) {
    String os = System.getProperty("os.name").toLowerCase();
    String home = System.getProperty("user.home");
    Path configPath = null;
    if (os.contains("win")) {
      String appData = System.getenv("APPDATA");
      configPath = Paths.get(appData, "spell", fileName);
    } else if (os.contains("mac")) {
      configPath = Paths.get(home, "Library", "Application Support", "spell", fileName);
    } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
      configPath = Paths.get(home, ".config", "spell", fileName);
    }
    return configPath;
  }

  private void createSpellConfigIfNeeded(Path configPath) {
    Path spellPath = configPath.getParent();
    if (!Files.exists(spellPath)) {
      try {
        Files.createDirectories(spellPath);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (!configPath.toFile().exists()) {
      saveConfigToFile(new SpellConfig(), configPath);
    }
  }

  private void saveConfigToFile(SpellConfig spellConfig, Path configPath) {
    try (FileWriter fileWriter = new FileWriter(configPath.toFile())) {
      objectMapper.writeValue(fileWriter, spellConfig);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
