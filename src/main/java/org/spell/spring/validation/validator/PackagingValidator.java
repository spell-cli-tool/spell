package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.service.InitializerService;
import org.spell.spring.validation.annotation.ValidPackaging;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class PackagingValidator extends AbstractConstraintValidator<ValidPackaging, String> {

  private final InitializerService service;
  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String packaging, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(packaging)) {
      return true;
    }

    var packages = service.retrievePackagingIds();

    if (!packages.contains(packaging)) {
      shellHelper.printError(
          String.format("Incorrect '%s' parameter value. Possible values: [%s]",
              InitializerConstant.PACKAGING_PARAM,
              String.join(", ", packages)));
      return false;
    }
    
    return true;
  }
}
