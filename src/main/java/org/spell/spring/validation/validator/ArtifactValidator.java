package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spell.common.ShellHelper;
import org.spell.spring.constant.InitializerConstant;
import org.spell.spring.validation.annotation.ValidArtifact;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ArtifactValidator extends AbstractConstraintValidator<ValidArtifact, String> {

  private final ShellHelper shellHelper;

  @Override
  public boolean isValid(String artifact, ConstraintValidatorContext constraintValidatorContext) {
    if (!StringUtils.hasText(artifact)) {
      return true;
    }

    if (!InitializerConstant.ARTIFACT_PATTERN.matcher(artifact).matches()) {
      shellHelper.printError(String.format("The value for '%s' parameter must match the pattern %s. "
              + "Example: demo",
          InitializerConstant.ARTIFACT_PARAM, InitializerConstant.ARTIFACT_PATTERN_VALUE));
      return false;
    }
    
    return true;
  }
}
