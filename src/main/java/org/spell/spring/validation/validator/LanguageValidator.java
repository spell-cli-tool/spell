package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.service.InitializerService;
import org.spell.spring.validation.annotation.ValidLanguage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LanguageValidator extends AbstractConstraintValidator<ValidLanguage, String> {

  private final InitializerService service;
  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String language, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(language)) {
      return true;
    }

    var languages = service.retrieveLanguageIds();

    if (!languages.contains(language)) {
      shellHelper.printError(
          String.format("Incorrect '%s' parameter value. Possible values: [%s]",
              InitializerConstant.LANGUAGE_PARAM,
              String.join(", ", languages)));
      return false;
    }
    
    return true;
  }
}
