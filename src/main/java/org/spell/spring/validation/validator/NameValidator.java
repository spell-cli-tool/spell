package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.validation.annotation.ValidName;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class NameValidator extends AbstractConstraintValidator<ValidName, String> {

  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(name)) {
      return true;
    }

    if (!InitializerConstant.NAME_PATTERN.matcher(name).matches()) {
      shellHelper.printError(String.format("The value for '%s' parameter must match the pattern %s. "
              + "Example: demo",
          InitializerConstant.NAME_PARAM, InitializerConstant.NAME_PATTERN_VALUE));
      return false;
    }
    
    return true;
  }
}
