package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.CommandConstant;
import org.spell.spring.service.InitializerService;
import org.spell.spring.validation.annotation.ValidBootVersion;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class BootVersionValidator extends AbstractConstraintValidator<ValidBootVersion, String> {

  private final InitializerService service;
  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String bootVersion, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(bootVersion)) {
      return true;
    }

    var versions = service.retrieveBootVersionIds();

    if (!versions.contains(bootVersion)) {
      shellHelper.printError(
          String.format("Incorrect '%s' parameter value. Possible values: [%s]",
              CommandConstant.BOOT_VERSION_PARAM,
              String.join(", ", versions)));
      return false;
    }

    return true;
  }
}
