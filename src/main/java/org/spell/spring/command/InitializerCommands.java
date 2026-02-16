package org.spell.spring.command;

import java.util.List;
import org.jline.utils.AttributedStyle;
import org.spell.common.BaseShellComponent;
import org.spell.common.FileManager;
import org.spell.common.ShellHelper;
import org.spell.spring.Action;
import org.spell.spring.RequestParam;
import org.spell.spring.client.model.DependenciesGroup;
import org.spell.spring.client.model.DependenciesValue;
import org.spell.spring.client.model.Guide;
import org.spell.spring.client.model.MetadataDto;
import org.spell.spring.client.model.Reference;
import org.spell.spring.config.Template;
import org.spell.spring.constant.CommandConstant;
import org.spell.spring.options.BootVersionValueProvider;
import org.spell.spring.options.DependencyValueProvider;
import org.spell.spring.options.JavaVersionValueProvider;
import org.spell.spring.options.LanguageValueProvider;
import org.spell.spring.options.PackagingValueProvider;
import org.spell.spring.options.TemplateValueProvider;
import org.spell.spring.options.TypeValueProvider;
import org.spell.spring.service.InitializerService;
import org.spell.spring.service.SpellConfigService;
import org.spell.spring.validation.annotation.ValidArtifact;
import org.spell.spring.validation.annotation.ValidBootVersion;
import org.spell.spring.validation.annotation.ValidDependencies;
import org.spell.spring.validation.annotation.ValidGroup;
import org.spell.spring.validation.annotation.ValidJavaVersion;
import org.spell.spring.validation.annotation.ValidLanguage;
import org.spell.spring.validation.annotation.ValidName;
import org.spell.spring.validation.annotation.ValidPackaging;
import org.spell.spring.validation.annotation.ValidType;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@ShellComponent
@ShellCommandGroup("Spring Initializer Commands")
public class InitializerCommands extends BaseShellComponent {

  private final InitializerService service;
  private final SpellConfigService configService;

  public InitializerCommands(InitializerService service,
      SpellConfigService configService,
      ShellHelper shellHelper,
      FileManager fileManager) {
    super(shellHelper, fileManager);
    this.service = service;
    this.configService = configService;
  }

  @ShellMethod(key = "dependency", value = "Show details of selected dependencies.")
  public void showDependenciesInfo() {

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


  @ShellMethod(key = "param", value = "Show values (examples) for params of the 'create' command.")
  public void showParamsValues() {
    Object[][] sampleData = new Object[][] {
        {"Param", "Values"},
        {String.format("%s, %s", CommandConstant.SHORT_TYPE_PARAM, CommandConstant.TYPE_PARAM),
          String.join(", ", service.retrieveTypeIds())},
        {String.format("%s, %s", CommandConstant.SHORT_LANGUAGE_PARAM, CommandConstant.LANGUAGE_PARAM),
            String.join(", ", service.retrieveLanguageIds())},
        {String.format("%s, %s", CommandConstant.SHORT_BOOT_VERSION_PARAM, CommandConstant.BOOT_VERSION_PARAM),
            String.join(", ", service.retrieveBootVersionIds())},
        {String.format("%s, %s", CommandConstant.SHORT_GROUP_PARAM, CommandConstant.GROUP_PARAM),
            service.defaultGroupId()},
        {String.format("%s, %s", CommandConstant.SHORT_ARTIFACT_PARAM, CommandConstant.ARTIFACT_PARAM),
            service.defaultArtifactId()},
        {String.format("%s, %s", CommandConstant.SHORT_FOLDER_PARAM, CommandConstant.FOLDER_PARAM),
            service.defaultArtifactId()},
        {String.format("%s, %s", CommandConstant.SHORT_PACKAGING_PARAM, CommandConstant.PACKAGING_PARAM),
            String.join(", ", service.retrievePackagingIds())},
        {String.format("%s, %s", CommandConstant.SHORT_JAVA_VERSION_PARAM, CommandConstant.JAVA_VERSION_PARAM),
            String.join(", ", service.retrieveJavaVersionIds())},
        {String.format("%s, %s", CommandConstant.SHORT_DEPENDENCIES_PARAM, CommandConstant.DEPENDENCIES_PARAM),
            String.join(", ", service.retrieveDependencyIds())},
    };
    TableModel model = new ArrayTableModel(sampleData);
    TableBuilder tableBuilder = new TableBuilder(model);
    tableBuilder.addFullBorder(BorderStyle.fancy_light);
    shellHelper.print(tableBuilder.build().render(30));
  }

  @ShellMethod(key = "create", value = "Create a Spring Boot project non-interactively with params.")
  public void create(
      @ValidType
      @ShellOption(
          value = {CommandConstant.SHORT_TYPE_PARAM, CommandConstant.TYPE_PARAM},
          valueProvider = TypeValueProvider.class,
          help = "Project type: Gradle or Maven",
          defaultValue = "") String type,
      @ValidLanguage
      @ShellOption(
          value = {CommandConstant.SHORT_LANGUAGE_PARAM, CommandConstant.LANGUAGE_PARAM},
          valueProvider = LanguageValueProvider.class,
          help = "Programming language: Java, Kotlin, Groovy",
          defaultValue = "") String language,
      @ValidBootVersion
      @ShellOption(
          value = {CommandConstant.SHORT_BOOT_VERSION_PARAM, CommandConstant.BOOT_VERSION_PARAM},
          valueProvider = BootVersionValueProvider.class,
          help = "Spring Boot version",
          defaultValue = "") String bootVersion,
      @ValidGroup
      @ShellOption(
          value = {CommandConstant.SHORT_GROUP_PARAM, CommandConstant.GROUP_PARAM},
          help = "Project metadata: group id (for example: com.example)",
          defaultValue = "") String groupId,
      @ValidArtifact
      @ShellOption(
          value = {CommandConstant.SHORT_ARTIFACT_PARAM, CommandConstant.ARTIFACT_PARAM},
          help = "Project metadata: artifact id (for example: demo)",
          defaultValue = "") String artifactId,
      @ValidName
      @ShellOption(
          value = {CommandConstant.SHORT_FOLDER_PARAM, CommandConstant.FOLDER_PARAM},
          help = "Project metadata: name. Project folder name (for example: demo)",
          defaultValue = "") String folder,
      @ValidPackaging
      @ShellOption(
          value = {CommandConstant.SHORT_PACKAGING_PARAM, CommandConstant.PACKAGING_PARAM},
          valueProvider = PackagingValueProvider.class,
          help = "Project metadata: packaging: Jar or War",
          defaultValue = "") String packaging,
      @ValidJavaVersion
      @ShellOption(
          value = {CommandConstant.SHORT_JAVA_VERSION_PARAM, CommandConstant.JAVA_VERSION_PARAM},
          valueProvider = JavaVersionValueProvider.class,
          help = "Project metadata: javaVersion",
          defaultValue = "") String javaVersion,
      @ValidDependencies
      @ShellOption(
          value = {CommandConstant.SHORT_DEPENDENCIES_PARAM, CommandConstant.DEPENDENCIES_PARAM},
          valueProvider = DependencyValueProvider.class,
          help = "Spring project dependencies",
          defaultValue = "") String dependencies,
      @ShellOption(
          value = {CommandConstant.TEMPLATE_PARAM},
          valueProvider = TemplateValueProvider.class,
          help = "Template name",
          defaultValue = "") String templateName) {
    StringBuilder params = new StringBuilder();

    Template template = configService.retrieveTemplate(templateName);

    if (template == null) {
      shellHelper.print(String.format("WARN: template with the name '%s' doesn't exist.", templateName),
          AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold());
    }

    type = configureType(type, template, params);
    language = configureLanguage(language, template, params);
    bootVersion = configureBootVersion(bootVersion, template, params);
    groupId = configureGroupId(groupId, template, params);
    artifactId = configureArtifactId(artifactId, template, params);
    folder = configureFolder(folder, artifactId, template, params);
    var packageName = configurePackageName(groupId, artifactId, params);
    packaging = configurePackaging(packaging, template, params);
    javaVersion = configureJavaVersion(javaVersion, template, params);
    dependencies = configureDependencies(dependencies, template, params);

    String actionValue = service.retrieveActionByTypeId(type);
    Action action = Action.getFromValue(actionValue);
    String projectName = service.download(action, params.toString());

    String resultType = "project";
    switch (action) {
      case STARTER_ZIP -> resultType = "project";
      case BUILD_GRADLE -> resultType = "gradle file";
      case POM_XML -> resultType = "pom file";
    }

    printCreateResultInfo("Project", type);
    printCreateResultInfo("Language", language);
    printCreateResultInfo("Spring Boot", bootVersion);
    printCreateResultInfo("Group", groupId);
    printCreateResultInfo("Artifact", artifactId);
    printCreateResultInfo("Name", folder);
    printCreateResultInfo("Package name", packageName);
    printCreateResultInfo("Packaging", packaging);
    printCreateResultInfo("Java", javaVersion);
    printCreateResultInfo("Dependencies", dependencies);

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
    String groupId = setInput("Group:", "com.example",
        CommandConstant.GROUP_PATTERN);
    params.append(toParam(RequestParam.GROUP_ID.getValue(), groupId));
    String artifactId = setInput("Artifact:", "demo",
        CommandConstant.ARTIFACT_PATTERN);
    params.append(toParam(RequestParam.ARTIFACT_ID.getValue(), artifactId));
    String name = setFolder("Folder:", artifactId);
    params.append(toParam(RequestParam.NAME.getValue(), name));
    params.append(toParam(RequestParam.BASE_DIR.getValue(), name));
    params.append(toParam(RequestParam.PACKAGE_NAME.getValue(),
        setInput("Package name:", groupId + "." + artifactId)));
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
    return selectSingleItem("Project:", items);
  }

  private String selectLanguage() {
    var items = service.retrieveLanguages();
    return selectSingleItem("Language:", items);
  }

  private String selectSpringBootVersion() {
    var items = service.retrieveSpringBootVersion();
    return selectSingleItem("Spring Boot:", items);
  }

  private String selectPackaging() {
    var items = service.retrievePackaging();
    return selectSingleItem("Packaging:", items);
  }

  private String selectJavaVersion() {
    var items = service.retrieveJavaVersion();
    return selectSingleItem("Java:", items);
  }

  private String selectMultipleDependencies() {
    var items = service.retrieveDependenciesWithGroups();
    return String.join(",", selectMultipleItems("Dependencies:", items));
  }

  private List<String> selectDependenciesForDetails() {
    var items = service.retrieveDependenciesWithoutGroups();
    return selectMultipleItems("Dependencies details:", items);
  }

  private String toParam(String param, String value) {
    return String.format("&%s=%s", param, value);
  }

  private void printCreateResultInfo(String param, String value) {
    shellHelper.print(
        String.format("%s %s %s",
            shellHelper.getStyledMessage("?",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold()),
            shellHelper.getStyledMessage(param, AttributedStyle.DEFAULT.bold()),
            shellHelper.getStyledMessage(value,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))));
  }

  private String configureType(String type, Template template, StringBuilder params) {
    if (!StringUtils.hasText(type)) {
      type = service.typeFromTemplateOrDefault(template);
    }
    params.append(String.format("?%s=%s", RequestParam.TYPE.getValue(), type));
    return type;
  }

  private String configureLanguage(String language, Template template, StringBuilder params) {
    if (!StringUtils.hasText(language)) {
      language = service.languageFromTemplateOrDefault(template);
    }
    params.append(toParam(RequestParam.LANGUAGE.getValue(), language));
    return language;
  }

  private String configureBootVersion(String bootVersion, Template template, StringBuilder params) {
    if (!StringUtils.hasText(bootVersion)) {
      bootVersion = service.bootVersionFromTemplateOrDefault(template);
    }
    params.append(toParam(RequestParam.BOOT_VERSION.getValue(), bootVersion));
    return bootVersion;
  }

  private String configureGroupId(String groupId, Template template, StringBuilder params) {
    if (!StringUtils.hasText(groupId)) {
      groupId = service.groupIdFromTemplateOrDefault(template);
    }
    params.append(toParam(RequestParam.GROUP_ID.getValue(), groupId));
    return groupId;
  }

  private String configureArtifactId(String artifactId, Template template, StringBuilder params) {
    if (!StringUtils.hasText(artifactId)) {
      artifactId = service.artifactIdFromTemplateOrDefault(template);
    }
    params.append(toParam(RequestParam.ARTIFACT_ID.getValue(), artifactId));
    return artifactId;
  }

  private String configureFolder(String folder, String artifactId, Template template, StringBuilder params) {
    if (!StringUtils.hasText(folder)) {
      if (template != null && StringUtils.hasText(template.getFolder())) {
        folder = template.getFolder();
      } else {
        folder = artifactId;
      }
    }
    params.append(toParam(RequestParam.NAME.getValue(), folder));
    params.append(toParam(RequestParam.BASE_DIR.getValue(), folder));
    return folder;
  }

  private String configurePackageName(String groupId, String artifactId, StringBuilder params) {
    String packageName = groupId + "." + artifactId;
    params.append(toParam(RequestParam.PACKAGE_NAME.getValue(), packageName));
    return packageName;
  }

  private String configurePackaging(String packaging, Template template, StringBuilder params) {
    if (!StringUtils.hasText(packaging)) {
      packaging = service.packagingFromTemplateOrDefault(template);
    }
    params.append(toParam(RequestParam.PACKAGING.getValue(), packaging));
    return packaging;
  }

  private String configureJavaVersion(String javaVersion, Template template, StringBuilder params) {
    if (!StringUtils.hasText(javaVersion)) {
      javaVersion = service.javaVersionFromTemplateOrDefault(template);
    }
    params.append(toParam(RequestParam.JAVA_VERSION.getValue(), javaVersion));
    return javaVersion;
  }

  private String configureDependencies(String dependencies, Template template, StringBuilder params) {
    if (!StringUtils.hasText(dependencies)) {
      dependencies = service.dependenciesFromTemplate(template);
    }
    params.append(toParam(RequestParam.DEPENDENCIES.getValue(), dependencies));
    return dependencies;
  }
}
