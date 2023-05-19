package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.service.InitializerService;
import org.spell.spring.validation.annotation.ValidDependencies;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class DependenciesValidator extends AbstractConstraintValidator<ValidDependencies, String> {

  private final InitializerService service;
  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String dependencies, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(dependencies)) {
      return true;
    }

    if (!InitializerConstant.DEPENDENCIES_PATTERN.matcher(dependencies).matches()
        || dependencies.contains(",,")
        || dependencies.lastIndexOf(",") == dependencies.length() - 1) {
      shellHelper.printError(
          String.format("The value for '%s' must match the pattern %s. Example: web,lombok,security",
          "--dependencies",
          InitializerConstant.DEPENDENCIES_PATTERN.pattern()));
      return false;
    }

    String wrongDependencies = findWrongDependencies(dependencies.split(","));

    if (StringUtils.hasText(wrongDependencies)) {
      shellHelper.printError(String.format("Wrong names of dependencies: %s", wrongDependencies));
      return false;
    }

    return true;
  }

  public String findWrongDependencies(String[] dependenciesForValidation) {
    var dependencies = service. retrieveDependencyIds();
    List<String> wrongDependencies = new ArrayList<>();

    for (var dependency : dependenciesForValidation) {
      if (!dependencies.contains(dependency)) {
        wrongDependencies.add(dependency);
      }
    }

    if (!CollectionUtils.isEmpty(wrongDependencies)) {
      return String.join( ", ", wrongDependencies);
    }

    return "";
  }
}
