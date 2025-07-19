package ru.vldaislab.bekrenev.authservice.annotations.email;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailPostfixValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailPostfix {
    String message() default "Email должен принадлежать домену {postfix}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String postfix() default "@company.ru";
}
