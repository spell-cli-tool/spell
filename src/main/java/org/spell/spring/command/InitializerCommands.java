package org.spell.spring.command;

import java.util.List;
import org.jline.utils.AttributedStyle;
import org.spell.BaseShellComponent;
import org.spell.ShellHelper;
import org.spell.spring.Action;
import org.spell.spring.client.model.DependenciesGroup;
import org.spell.spring.client.model.DependenciesValue;
import org.spell.spring.client.model.Guide;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.Reference;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.service.InitializerService;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.CollectionUtils;

@ShellComponent
@ShellCommandGroup("Spring Initializer Commands")
public class InitializerCommands extends BaseShellComponent {

  private final InitializerService service;

  public InitializerCommands(InitializerService service, ShellHelper shellHelper) {
    super(shellHelper);
    this.service = service;
  }

  @ShellMethod(key = "dependency", value = "Show details of selected dependencies.")
  public void dependency() {

    MetadataDto metadata = service.retrieveMetadata();
    List<String> names = selectDependenciesForDetails();

    shellHelper.emptyLine();

    for (String name : names) {
      dependenciesLoop:
      for (DependenciesGroup group : metadata.getDependencies().getValues()) {
        for (DependenciesValue value : group.getValues()) {
          if (value.getId().equals(name)) {
            shellHelper.print(value.getName(),
                AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA).bold().underline());
            shellHelper.print(value.getDescription());
            shellHelper.emptyLine();
            if (value.getLinks() != null) {
              List<Guide> guides = value.getLinks().retrieveGuides();
              if (!CollectionUtils.isEmpty(guides)) {
                shellHelper.print("Guides:",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold().underline());
                for (Guide guide : guides) {
                  shellHelper.print(guide.getTitle());
                  shellHelper.print(guide.getHref());
                }
                shellHelper.emptyLine();
              }

              List<Reference> references = value.getLinks().retrieveReferences();
              if (!CollectionUtils.isEmpty(references)) {
                shellHelper.print("References:",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold().underline());
                for (Reference reference : references) {
                  if (!reference.getTemplated()) {
                    shellHelper.print(reference.getHref());
                  } else {
                    metadata
                        .getBootVersion()
                        .getValues()
                        .forEach(bv -> shellHelper.print(
                            reference.getHref().replace("{bootVersion}", bv.getId()))
                        );
                  }
                }
                shellHelper.emptyLine();
              }
            }
            break dependenciesLoop;
          }
        }
      }
    }
  }

  @ShellMethod(key = "create", value = "Create Spring Boot project.")
  public void create() {
    StringBuilder params = new StringBuilder();
    String type = selectType();
    String action = service.retrieveActionByTypeId(type);
    params.append("?type=" + type);
    params.append("&language=" + selectLanguage());
    params.append("&bootVersion=" + selectSpringBootVersion());
    String groupId = setInput("Group", "com.example",
        InitializerConstant.GROUP_PATTERN);
    params.append("&groupId=" + groupId);
    String artifactId = setInput("Artifact", "demo",
        InitializerConstant.ARTIFACT_PATTERN);
    params.append("&artifactId=" + artifactId);
    String name = setInput("Name", artifactId);
    params.append("&name=" + name);
    params.append("&baseDir=" + name);
    params.append("&description="
        + setInput("Description", "Demo project for Spring Boot"));
    params.append("&packageName="
        + setInput("Package name", groupId + "." + artifactId));
    params.append("&packaging=" + selectPackaging());
    params.append("&javaVersion=" + selectJavaVersion());
    params.append("&dependencies="  + selectMultipleDependencies());

    service.download(Action.getFromValue(action), params.toString());

    shellHelper.print(String.format("The project '%s' is successfully created!", name),
        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold().underline());
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
    var items = service.retrieveDependenciesWithGroups();
    return String.join(",", selectMultipleItems("Dependencies", items));
  }

  private List<String> selectDependenciesForDetails() {
    var items = service.retrieveDependenciesWithoutGroups();
    return selectMultipleItems("Dependencies details", items);
  }

}
