package org.spell.spring;

import lombok.RequiredArgsConstructor;
import org.spell.spring.service.InitializerService;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("Spring Initializer Commands")
@RequiredArgsConstructor
public class InitializerCommands extends BaseShellComponent {

  private final InitializerService service;

  @ShellMethod(key = "info", value = "Show Spring Initializer settings details")
  public String info() {
    return service.retrieveSettingsInfo();
  }

  @ShellMethod(key = "create", value = "Create Spring Boot project")
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
    request.append("&dependencies="  + selectDependencies());

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

  private String selectDependencies() {
    var items = service.retrieveDependencies();
    return String.join(",", selectMultipleItems("Dependencies", items));
  }

}
