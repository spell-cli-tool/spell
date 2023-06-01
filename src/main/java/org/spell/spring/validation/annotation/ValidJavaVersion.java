package org.spell.spring.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.spell.spring.validation.validator.JavaVersionValidator;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JavaVersionValidator.class)
public @interface ValidJavaVersion {

  String message() default "Invalid value";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
