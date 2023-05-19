package org.spell.spring.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public abstract class AbstractConstraintValidator<A extends Annotation, T> implements
    ConstraintValidator<A, T> {
  protected String fieldName;

  @Override
  public void initialize(A constraintAnnotation) {
  }

  @Override
  public abstract boolean isValid(T t, ConstraintValidatorContext constraintValidatorContext);
}
