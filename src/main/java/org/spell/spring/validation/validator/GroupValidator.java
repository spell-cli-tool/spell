package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.validation.annotation.ValidGroup;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class GroupValidator extends AbstractConstraintValidator<ValidGroup, String> {

  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String group, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(group)) {
      return true;
    }

    if (!InitializerConstant.GROUP_PATTERN.matcher(group).matches()) {
      shellHelper.printError(String.format("The value for '%s' parameter must match the pattern %s. "
              + "Example: com.example",
          InitializerConstant.GROUP_PARAM, InitializerConstant.GROUP_PATTERN_VALUE));
      return false;
    }
    
    return true;
  }
}
