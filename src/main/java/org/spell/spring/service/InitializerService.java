package org.spell.spring.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;
import org.spell.common.FileManager;
import org.spell.common.ShellHelper;
import org.spell.spring.Action;
import org.spell.spring.client.InitializerClient;
import org.spell.spring.client.model.DependenciesGroup;
import org.spell.spring.client.model.DependenciesValue;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.MetadataValue;
import org.spell.spring.client.model.TypeValue;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitializerService {

  private final InitializerClient client;
  private final FileManager fileManager;
  private final ShellHelper shellHelper;
  private MetadataDto metadata;

  public MetadataDto retrieveMetadata() {
    if (metadata == null) {
      metadata = client.retrieveMetadata();
    }

    return metadata;
  }

  public String defaultType() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getType().getDefaultValue();
  }

  public String defaultLanguage() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getLanguage().getDefaultValue();
  }

  public String defaultBootVersion() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getBootVersion().getDefaultValue();
  }

  public String defaultGroupId() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getGroupId().getDefaultValue();
  }

  public String defaultArtifactId() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getArtifactId().getDefaultValue();
  }

  public String defaultDescription() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getDescription().getDefaultValue();
  }

  public String defaultPackaging() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getPackaging().getDefaultValue();
  }

  public String defaultJavaVersion() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getJavaVersion().getDefaultValue();
  }

  public List<SelectorItem<String>> retrieveTypes() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getType().getDefaultValue(), metadata.getType().getValues());
  }

  public List<String> retrieveTypeIds() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getType().getValues()
        .stream().map(MetadataValue::getId).collect(Collectors.toList());
  }

  public List<String> retrieveLanguageIds() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getLanguage().getValues()
        .stream().map(MetadataValue::getId).collect(Collectors.toList());
  }

  public List<String> retrieveBootVersionIds() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getBootVersion().getValues()
        .stream().map(MetadataValue::getId).collect(Collectors.toList());
  }

  public List<String> retrievePackagingIds() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getPackaging().getValues()
        .stream().map(MetadataValue::getId).collect(Collectors.toList());
  }

  public List<String> retrieveJavaVersionIds() {
    MetadataDto metadata = retrieveMetadata();
    return metadata.getJavaVersion().getValues()
        .stream().map(MetadataValue::getId).collect(Collectors.toList());
  }

  public String retrieveActionByTypeId(String typeId) {
    MetadataDto metadata = retrieveMetadata();
    TypeValue typeValue = metadata.getType().getValues().stream()
      .filter(type -> type.getId().equals(typeId))
      .findFirst()
      .orElse(new TypeValue().setAction("/starter.zip"));
    return typeValue.getAction();
  }

  public List<SelectorItem<String>> retrieveLanguages() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getLanguage().getDefaultValue(),
        metadata.getLanguage().getValues());
  }

  public List<SelectorItem<String>> retrieveSpringBootVersion() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getBootVersion().getDefaultValue(),
        metadata.getBootVersion().getValues());
  }

  public List<SelectorItem<String>> retrievePackaging() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getPackaging().getDefaultValue(),
        metadata.getPackaging().getValues());
  }

  public List<SelectorItem<String>> retrieveJavaVersion() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getJavaVersion().getDefaultValue(),
        metadata.getJavaVersion().getValues());
  }

  public List<SelectorItem<String>> retrieveDependenciesWithGroups() {
    var result = new ArrayList<SelectorItem<String>>();
    MetadataDto metadata = retrieveMetadata();

    for (DependenciesGroup group : metadata.getDependencies().getValues()) {
      result.add(SelectorItem.of(
          shellHelper.getStyledMessage(String.format("########## %s ##########", group.getName()),
              AttributedStyle.DEFAULT.foreground(AttributedStyle.BRIGHT).bold()),
          group.getId(), false, false));
      for (DependenciesValue value : group.getValues()) {
        result.add(SelectorItem.of(
            String.format("%s - %s", shellHelper.getStyledMessage(value.getName(),
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)),
                value.getDescription()), value.getId()));
      }
    }
    return result;
  }

  public List<SelectorItem<String>> retrieveDependenciesWithoutGroups() {
    var result = new ArrayList<SelectorItem<String>>();
    MetadataDto metadata = retrieveMetadata();

    for (DependenciesGroup group : metadata.getDependencies().getValues()) {
      for (DependenciesValue value : group.getValues()) {
        result.add(SelectorItem.of(value.getName(), value.getId()));
      }
    }
    return result;
  }

  public List<String> retrieveDependencyIds() {
    var result = new ArrayList<String>();
    MetadataDto metadata = retrieveMetadata();
    for (DependenciesGroup group : metadata.getDependencies().getValues()) {
      for (DependenciesValue value : group.getValues()) {
        result.add(value.getId());
      }
    }
    return result;
  }

  public String download(Action action, String params) {
    String projectName = "";

    switch (action) {
      case STARTER_ZIP -> projectName = downloadProject(params);
      case BUILD_GRADLE -> projectName = downLoadBuildGradle(params);
      case POM_XML -> projectName = downLoadPomXml(params);
    }

    return projectName;
  }

  public String downLoadBuildGradle(String request) {
    byte[] file = client.download(Action.BUILD_GRADLE, request);
    String name = fileManager.findUniqueFileName(
        "build", "gradle", " (%s).gradle");
    saveFile(name, file);
    return name;
  }

  public String downLoadPomXml(String params) {
    byte[] file = client.download(Action.POM_XML, params);
    String name = fileManager.findUniqueFileName(
        "pom", "xml", " (%s).xml");
    saveFile(name, file);
    return name;
  }

  public String downloadProject(String params) {
    String projectDirectoryName = "";

    byte[] zip = client.download(Action.STARTER_ZIP, params);

    File destDir = new File(fileManager.getCurrentDirectoryPath());
    byte[] buffer = new byte[1024];

    try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip))) {
      ZipEntry zipEntry = zis.getNextEntry();
      String initProjectDirectoryName = "";

      if (zipEntry != null) {
        initProjectDirectoryName = zipEntry.getName().replace("/", "");
        projectDirectoryName = initProjectDirectoryName;
        if (fileManager.getCurrentDirectoryPath().endsWith(projectDirectoryName)
            && fileManager.isCurrentDirectoryEmpty()) {
          destDir = new File(fileManager.getParentDirectoryPath());
        } else {
          projectDirectoryName = fileManager.findUniqueDirectoryName(projectDirectoryName);
        }
      }

      while (zipEntry != null) {
        String name = zipEntry.getName().replaceFirst(initProjectDirectoryName, projectDirectoryName);
        File newFile = newFile(destDir, name);
        if (zipEntry.isDirectory()) {
          if (!newFile.isDirectory() && !newFile.mkdirs()) {
            throw new IOException("Failed to create directory " + newFile);
          }
        } else {
          File parent = newFile.getParentFile();
          if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent);
          }

          FileOutputStream fos = new FileOutputStream(newFile);
          int len;
          while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
          }
          fos.close();
        }
        zipEntry = zis.getNextEntry();
      }

      zis.closeEntry();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return projectDirectoryName;
  }

  private File newFile(File destinationDir, String name) throws IOException {
    File destFile = new File(destinationDir, name);

    String destDirPath = destinationDir.getCanonicalPath();
    String destFilePath = destFile.getCanonicalPath();

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
      throw new IOException("Entry is outside of the target dir: " + name);
    }

    return destFile;
  }

  private <T extends MetadataValue> List<SelectorItem<String>> retrieveSelectorItems(
      String defaultId, List<T> values) {
    List<SelectorItem<String>> items = new ArrayList<>();
    Optional<SelectorItem<String>> defaultItem = values
      .stream()
      .filter(value -> defaultId.equals(value.getId()))
      .map(value -> SelectorItem.of(value.getName() + " [default]", value.getId()))
      .findFirst();

    defaultItem.ifPresent(items::add);

    values
      .stream()
      .filter(value -> !defaultId.equals(value.getId()))
      .forEach(value -> items.add(SelectorItem.of(value.getName(), value.getId())));

    return items;
  }

  private void saveFile(String name, byte[] file) {
    File outputFile = new File(name);
    try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
      outputStream.write(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
