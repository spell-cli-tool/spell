package org.spell.spring.command;

import jakarta.validation.constraints.NotBlank;
import org.jline.utils.AttributedStyle;
import org.spell.common.BaseShellComponent;
import org.spell.common.FileManager;
import org.spell.common.ShellHelper;
import org.spell.spring.config.Template;
import org.spell.spring.constant.CommandConstant;
import org.spell.spring.options.BootVersionValueProvider;
import org.spell.spring.options.DependencyValueProvider;
import org.spell.spring.options.JavaVersionValueProvider;
import org.spell.spring.options.LanguageValueProvider;
import org.spell.spring.options.PackagingValueProvider;
import org.spell.spring.options.TypeValueProvider;
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

@ShellComponent
@ShellCommandGroup("Spell configuration commands")
public class ConfigCommands extends BaseShellComponent {

  private final SpellConfigService service;

  public ConfigCommands(ShellHelper shellHelper,
      FileManager fileManager, SpellConfigService service) {
    super(shellHelper, fileManager);
    this.service = service;
  }

  @ShellMethod(key = "config", value = "Show configuration.")
  public void showConfig() {
    print("Config path: ", service.retrievePath());
    shellHelper.print(service.retrieveAsString());
  }

  @ShellMethod(key = "set-group", value = "Set default group name.")
  public void setDefaultGroup(
      @ValidGroup
      @ShellOption(defaultValue = "") String defaultGroup) {
    service.setDefaultGroup(defaultGroup);
    print("Default group name: ", defaultGroup);
  }

  @ShellMethod(key = "set-artifact", value = "Set default artifact name.")
  public void setDefaultArtifact(
      @ValidArtifact
      @ShellOption(defaultValue = "") String defaultArtifact) {
    service.setDefaultArtifact(defaultArtifact);
    print("Default artifact name: ", defaultArtifact);
  }

  @ShellMethod(key = "set-template", value = "Create/replace template.")
  public void setTemplate(
      @NotBlank
      @ShellOption(
          value = {CommandConstant.SHORT_TEMPLATE_NAME_PARAM, CommandConstant.TEMPLATE_NAME_PARAM},
          help = "Template name",
          defaultValue = "") String name,
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
          value = {CommandConstant.REPLACE_PARAM, CommandConstant.SHORT_REPLACE_PARAM},
          help = "Replace template with the same name",
          defaultValue = "false") Boolean replace) {
    Template template = new Template()
        .setName(name)
        .setType(type)
        .setLanguage(language)
        .setBootVersion(bootVersion)
        .setGroup(groupId)
        .setArtifact(artifactId)
        .setFolder(folder)
        .setPackaging(packaging)
        .setJavaVersion(javaVersion)
        .setDependencies(dependencies);
    if (!replace && service.isTemplateExist(template.getName())) {
      boolean rewrite = setConfirmationInput(
          String.format("Template '%s' already exists. Do you want to replace?",
              template.getName()),
          false);
      if (rewrite) {
        service.createOrReplaceTemplate(template);
        shellHelper.print(String.format("Template '%s' is successfully replaced!", name),
            AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold());
      }
    } else {
      service.createOrReplaceTemplate(template);
      shellHelper.print(String.format("Template '%s' is successfully created!", name),
          AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold());
    }
  }

  @ShellMethod(key = "remove-template", value = "Remove template.")
  public void setTemplate(
      @ShellOption(
          value = {CommandConstant.SHORT_TEMPLATE_NAME_PARAM, CommandConstant.TEMPLATE_NAME_PARAM},
          help = "Template name",
          defaultValue = "template") String name) {
    boolean result = service.deleteTemplate(name);
    if (result) {
      shellHelper.print(String.format("Template '%s' is successfully created!", name),
          AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold());
    } else {
      shellHelper.print(String.format("Template '%s' doesn't exist!", name),
          AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold());
    }
  }

  private void print(String name, String value) {
    shellHelper.print(String.format("%s%s", shellHelper.getStyledMessage(name,
        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold()), value));
  }
}
