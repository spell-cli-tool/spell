package org.spell.spring.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.spell.spring.validation.validator.GroupValidator;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GroupValidator.class)
public @interface ValidGroup {

  String message() default "Invalid value";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
