package org.spell.spring.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import org.spell.spring.client.InitializerClient;
import org.spell.spring.client.InitializerRestProperties;
import org.spell.spring.client.model.DependenciesGroup;
import org.spell.spring.client.model.DependenciesValue;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.MetadataValue;
import org.spell.spring.client.model.TypeValue;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class InitializerService {

  private final InitializerRestProperties properties;
  private final InitializerClient client;

  private String settingsInfo;
  private MetadataDto metadata;

  public String retrieveSettingsInfo() {
    if (!StringUtils.hasText(settingsInfo)) {
      settingsInfo = client.retrieveSettingsInfo();
    }
    return settingsInfo;
  }

  public MetadataDto retrieveMetadata() {
    if (metadata == null) {
      metadata = client.retrieveMetadata();
    }

    return metadata;
  }

  public List<SelectorItem<String>> retrieveTypes() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getType().getValues());
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
    return retrieveSelectorItems(metadata.getLanguage().getValues());
  }

  public List<SelectorItem<String>> retrieveSpringBootVersion() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getBootVersion().getValues());
  }

  public List<SelectorItem<String>> retrievePackaging() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getPackaging().getValues());
  }

  public List<SelectorItem<String>> retrieveJavaVersion() {
    MetadataDto metadata = retrieveMetadata();
    return retrieveSelectorItems(metadata.getJavaVersion().getValues());
  }

  public List<SelectorItem<String>> retrieveDependencies() {
    var result = new ArrayList<SelectorItem<String>>();
    MetadataDto metadata = retrieveMetadata();

    for (DependenciesGroup group : metadata.getDependencies().getValues()) {
      result.add(SelectorItem.of(String.format("<======%s======>", group.getName()),
          group.getId(), false, false));
      for (DependenciesValue value : group.getValues()) {
        result.add(SelectorItem.of(value.getName(), value.getId()));
      }
    }
    return result;
  }

  public void downloadProject(String request) {
    byte[] zip = client.downloadProject(request);

    File destDir = new File(System.getProperty("user.dir"));
    byte[] buffer = new byte[1024];

    try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip))) {
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        File newFile = newFile(destDir, zipEntry);
        if (zipEntry.isDirectory()) {
          if (!newFile.isDirectory() && !newFile.mkdirs()) {
            throw new IOException("Failed to create directory " + newFile);
          }
        } else {
          // fix for Windows-created archives
          File parent = newFile.getParentFile();
          if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent);
          }

          // write file content
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
  }

  private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
    File destFile = new File(destinationDir, zipEntry.getName());

    String destDirPath = destinationDir.getCanonicalPath();
    String destFilePath = destFile.getCanonicalPath();

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
      throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
    }

    return destFile;
  }

  private <T extends MetadataValue> List<SelectorItem<String>> retrieveSelectorItems(List<T> values) {
    return values
        .stream()
        .map(type -> SelectorItem.of(type.getName(), type.getId()))
        .collect(Collectors.toList());
  }


}