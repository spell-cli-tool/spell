package org.spell.spring.command;

import java.util.List;
import org.jline.utils.AttributedStyle;
import org.spell.common.BaseShellComponent;
import org.spell.common.ShellHelper;
import org.spell.spring.Action;
import org.spell.spring.RequestParam;
import org.spell.spring.client.model.DependenciesGroup;
import org.spell.spring.client.model.DependenciesValue;
import org.spell.spring.client.model.Guide;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.Reference;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.options.BootVersionValueProvider;
import org.spell.spring.options.DependencyValueProvider;
import org.spell.spring.options.JavaVersionValueProvider;
import org.spell.spring.options.LanguageValueProvider;
import org.spell.spring.options.PackagingValueProvider;
import org.spell.spring.options.TypeValueProvider;
import org.spell.spring.service.InitializerService;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

  @ShellMethod(key = "create", value = "Create a Spring Boot project with params.")
  public void create(
      @ShellOption(
          value = {"-t", "--type"},
          valueProvider = TypeValueProvider.class,
          help = "Project type: Gradle or Maven",
          defaultValue = "") String type,
      @ShellOption(
          value = {"-l", "--language"},
          valueProvider = LanguageValueProvider.class,
          help = "Programming language: Java, Kotlin, Groovy",
          defaultValue = "") String language,
      @ShellOption(
          value = {"-b", "--boot-version"},
          valueProvider = BootVersionValueProvider.class,
          help = "Spring Boot version",
          defaultValue = "") String bootVersion,
      @ShellOption(
          value = {"-g", "--group-id"},
          help = "Project metadata: group id (for example: com.example)",
          defaultValue = "") String groupId,
      @ShellOption(
          value = {"-a", "--artifact-id"},
          help = "Project metadata: artifact id (for example: demo)",
          defaultValue = "") String artifactId,
      @ShellOption(
          value = {"-n", "--name"},
          help = "Project metadata: name. Project folder name (for example: demo)",
          defaultValue = "") String name,
      @ShellOption(
          value = {"--description"},
          help = "Project metadata: description (for example: Demo project for Spring Boot)",
          defaultValue = "") String description,
      @ShellOption(
          value = {"--package-name"},
          help = "Project metadata: packageName (for example: com.example.demo)",
          defaultValue = "") String packageName,
      @ShellOption(
          value = {"-p", "--packaging"},
          valueProvider = PackagingValueProvider.class,
          help = "Project metadata: packaging: Jar or War",
          defaultValue = "") String packaging,
      @ShellOption(
          value = {"-j", "--java-version"},
          valueProvider = JavaVersionValueProvider.class,
          help = "Project metadata: javaVersion",
          defaultValue = "") String javaVersion,
      @ShellOption(
          value = {"-d", "--dependencies"},
          valueProvider = DependencyValueProvider.class,
          help = "Spring project dependencies",
          defaultValue = "") String dependencies) {
    StringBuilder params = new StringBuilder();

    if (!StringUtils.hasText(type)) {
      type = service.defaultType();
    }
    String actionValue = service.retrieveActionByTypeId(type);
    params.append(String.format("?%s=%s", RequestParam.TYPE.getValue(), type));

    if (!StringUtils.hasText(language)) {
      language = service.defaultLanguage();
    }
    params.append(toParam(RequestParam.LANGUAGE.getValue(), language));

    if (!StringUtils.hasText(bootVersion)) {
      bootVersion = service.defaultBootVersion();
    }
    params.append(toParam(RequestParam.BOOT_VERSION.getValue(), bootVersion));

    if (!StringUtils.hasText(groupId)) {
      groupId = service.defaultGroupId();
    }
    params.append(toParam(RequestParam.GROUP_ID.getValue(), groupId));

    if (!StringUtils.hasText(artifactId)) {
      artifactId = service.defaultArtifactId();
    }
    params.append(toParam(RequestParam.ARTIFACT_ID.getValue(), artifactId));

    if (!StringUtils.hasText(name)) {
      name = artifactId;
    }
    params.append(toParam(RequestParam.NAME.getValue(), name));
    params.append(toParam(RequestParam.BASE_DIR.getValue(), name));

    if (!StringUtils.hasText(description)) {
      description = service.defaultDescription();
    }
    params.append(toParam(RequestParam.DESCRIPTION.getValue(), description));

    if (!StringUtils.hasText(packageName)) {
      packageName = groupId + "." + artifactId;
    }
    params.append(toParam(RequestParam.PACKAGE_NAME.getValue(), packageName));

    if (!StringUtils.hasText(packaging)) {
      packaging = service.defaultPackaging();
    }
    params.append(toParam(RequestParam.PACKAGING.getValue(), packaging));

    if (!StringUtils.hasText(javaVersion)) {
      javaVersion = service.defaultJavaVersion();
    }
    params.append(toParam(RequestParam.JAVA_VERSION.getValue(), javaVersion));

    params.append(toParam(RequestParam.DEPENDENCIES.getValue(), dependencies));

    Action action = Action.getFromValue(actionValue);
    String projectName = service.download(action, params.toString());

    String resultType = "project";
    switch (action) {
      case STARTER_ZIP -> resultType = "project";
      case BUILD_GRADLE -> resultType = "gradle file";
      case POM_XML -> resultType = "pom file";
    }
    shellHelper.print(String.format("The %s '%s' is successfully created!", resultType, projectName),
        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold());
  }

  @ShellMethod(key = "icreate", value = "Create a Spring Boot project interactively.")
  public void interactiveCreate() {
    StringBuilder params = new StringBuilder();
    String type = selectType();
    String actionValue = service.retrieveActionByTypeId(type);
    params.append(String.format("?%s=%s", RequestParam.TYPE.getValue(), type));
    params.append(toParam(RequestParam.LANGUAGE.getValue(), selectLanguage()));
    params.append(toParam(RequestParam.BOOT_VERSION.getValue(), selectSpringBootVersion()));
    String groupId = setInput("Group", "com.example",
        InitializerConstant.GROUP_PATTERN);
    params.append(toParam(RequestParam.GROUP_ID.getValue(), groupId));
    String artifactId = setInput("Artifact", "demo",
        InitializerConstant.ARTIFACT_PATTERN);
    params.append(toParam(RequestParam.ARTIFACT_ID.getValue(), artifactId));
    String name = setInput("Name", artifactId);
    params.append(toParam(RequestParam.NAME.getValue(), name));
    params.append(toParam(RequestParam.BASE_DIR.getValue(), name));
    params.append(toParam(RequestParam.DESCRIPTION.getValue(),
        setInput("Description", "Demo project for Spring Boot")));
    params.append(toParam(RequestParam.PACKAGE_NAME.getValue(),
        setInput("Package name", groupId + "." + artifactId)));
    params.append(toParam(RequestParam.PACKAGING.getValue(), selectPackaging()));
    params.append(toParam(RequestParam.JAVA_VERSION.getValue(), selectJavaVersion()));
    params.append(toParam(RequestParam.DEPENDENCIES.getValue(), selectMultipleDependencies()));

    Action action = Action.getFromValue(actionValue);
    String projectName = service.download(action, params.toString());

    String resultType = "project";
    switch (action) {
      case STARTER_ZIP -> resultType = "project";
      case BUILD_GRADLE -> resultType = "gradle file";
      case POM_XML -> resultType = "pom file";
    }
    shellHelper.print(String.format("The %s '%s' is successfully created!", resultType, projectName),
        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold());
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

  private String toParam(String param, String value) {
    return String.format("&%s=%s", param, value);
  }
}
