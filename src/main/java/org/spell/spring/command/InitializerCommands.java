package org.spell.spring.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;
import org.spell.BaseShellComponent;
import org.spell.ShellHelper;
import org.spell.spring.DependencyValuesProvider;
import org.spell.spring.client.model.DependenciesGroup;
import org.spell.spring.client.model.DependenciesValue;
import org.spell.spring.client.model.Guide;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.Reference;
import org.spell.spring.service.InitializerService;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@ShellComponent
@ShellCommandGroup("Spring Initializer Commands")
@RequiredArgsConstructor
public class InitializerCommands extends BaseShellComponent {

  private final InitializerService service;
  private final ShellHelper shellHelper;

  @ShellMethod(key = "dependency", value = "Show dependency details.")
  public void dependency(
      @ShellOption(valueProvider = DependencyValuesProvider.class,
          value = {"-n", "--name"}, defaultValue = "") String name) {

    MetadataDto metadata = service.retrieveMetadata();

    if (!StringUtils.hasText(name)) {
        name = selectSingleDependency();
    }

    shellHelper.emptyLine();

    for (DependenciesGroup group : metadata.getDependencies().getValues()) {
      for (DependenciesValue value : group.getValues()) {
        if (value.getId().equals(name)) {
          shellHelper.print(value.getName(), AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA).bold().underline());
          shellHelper.print(value.getDescription());
          shellHelper.emptyLine();
          if (value.getLinks() != null) {
            List<Guide> guides = value.getLinks().retrieveGuides();
            if (!CollectionUtils.isEmpty(guides)) {
              shellHelper.print("Guides:", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold().underline());
              for (Guide guide : guides) {
                shellHelper.print(guide.getTitle());
                shellHelper.print(guide.getHref());
              }
              shellHelper.emptyLine();
            }

            List<Reference> references = value.getLinks().retrieveReferences();
            if (!CollectionUtils.isEmpty(references)) {
              shellHelper.print("References:", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold().underline());
              for (Reference reference : references) {
                if (!reference.getTemplated()) {
                  shellHelper.print(reference.getHref());
                } else {
                  metadata
                    .getBootVersion()
                    .getValues()
                    .forEach(bv -> shellHelper.print(reference.getHref().replace("{bootVersion}", bv.getId()))
                    );
                }
              }
            }
          }
          shellHelper.emptyLine();
          return;
        }
      }
    }
  }

  @ShellMethod(key = "info", value = "Show all Spring Initializer settings details.")
  public String info() {
    return service.retrieveSettingsInfo();
  }

  @ShellMethod(key = "create", value = "Create Spring Boot project.")
  public String create() {
    StringBuilder request = new StringBuilder();
    String type = selectType();
    String action = service.retrieveActionByTypeId(type);
    request.append(action);
    request.append("?type=" + type);
    request.append("&language=" + selectLanguage());
    request.append("&bootVersion=" + selectSpringBootVersion());
    String groupId = setInput("Group", "com.example");
    request.append("&groupId=" + groupId);
    String artifactId = setInput("Artifact", "demo");
    request.append("&artifactId=" + artifactId);
    String name = setInput("Name", artifactId);
    request.append("&name=" + name);
    request.append("&baseDir=" + name);
    request.append("&description="
        + setInput("Description", "Demo project for Spring Boot")
        .replace(" ", "%20"));
    request.append("&packageName="
        + setInput("Package name", groupId + "." + artifactId));
    request.append("&packaging=" + selectPackaging());
    request.append("&javaVersion=" + selectJavaVersion());
    request.append("&dependencies="  + selectMultipleDependencies());

    service.downloadProject(request.toString());

    return String.format("The project '%s' is successfully created!", name);
  }

  private String selectType() {
    var items = service.retrieveTypes();
    return selectSingleItem("Project", items);
  }

  private String selectLanguage() {
    var items = service.retrieveLanguages();
    return selectSingleItem("Language", items);
  }

  private String selectSpringBootVersion() {
    var items = service.retrieveSpringBootVersion();
    return selectSingleItem("Spring Boot", items);
  }

  private String selectPackaging() {
    var items = service.retrievePackaging();
    return selectSingleItem("Packaging", items);
  }

  private String selectJavaVersion() {
    var items = service.retrieveJavaVersion();
    return selectSingleItem("Java", items);
  }

  private String selectMultipleDependencies() {
    var items = service.retrieveDependenciesForSelection();
    return String.join(",", selectMultipleItems("Dependencies", items));
  }

  private String selectSingleDependency() {
    var items = service.retrieveDependenciesForSingleSelection();
    return selectSingleItem("Dependencies details", items);
  }

}
