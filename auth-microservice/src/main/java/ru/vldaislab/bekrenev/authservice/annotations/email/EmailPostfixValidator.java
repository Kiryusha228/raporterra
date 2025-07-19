package ru.vldaislab.bekrenev.authservice.annotations.email;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class EmailPostfixValidator implements ConstraintValidator<EmailPostfix, String> {

    private String postfix;

    @Override
    public void initialize(EmailPostfix constraintAnnotation) {
        this.postfix = constraintAnnotation.postfix();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && email.endsWith(postfix);
    }
}