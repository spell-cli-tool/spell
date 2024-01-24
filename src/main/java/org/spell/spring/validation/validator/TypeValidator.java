package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.CommandConstant;
import org.spell.spring.service.InitializerService;
import org.spell.spring.validation.annotation.ValidType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TypeValidator extends AbstractConstraintValidator<ValidType, String> {

  private final InitializerService service;
  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String type, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(type)) {
      return true;
    }

    var types = service.retrieveTypeIds();

    if (!types.contains(type)) {
      shellHelper.printError(
          String.format("Incorrect '%s' parameter value. Possible values: [%s]",
              CommandConstant.TYPE_PARAM,
              String.join(", ", types)));
      return false;
    }

    return true;
  }
}
