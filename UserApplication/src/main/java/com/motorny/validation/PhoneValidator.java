package com.motorny.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "\\+380\\d{2}[-.]?\\d{3}[-.]?\\d{2}[-.]?\\d{2}\\b";
    private static final Pattern PATTERN = Pattern.compile(PHONE_PATTERN);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return validateEmail(phone);
    }

    private boolean validateEmail(String phone) {
        Matcher matcher = PATTERN.matcher(phone);
        return matcher.matches();
    }
}
