package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.service.InitializerService;
import org.spell.spring.validation.annotation.ValidJavaVersion;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JavaVersionValidator extends AbstractConstraintValidator<ValidJavaVersion, String> {

  private final InitializerService service;
  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String javaVersion, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(javaVersion)) {
      return true;
    }

    var versions = service.retrieveJavaVersionIds();

    if (!versions.contains(javaVersion)) {
      shellHelper.printError(
          String.format("Incorrect '%s' parameter value. Possible values: [%s]",
              InitializerConstant.JAVA_VERSION_PARAM,
              String.join(", ", versions)));
      return false;
    }
    
    return true;
  }
}
