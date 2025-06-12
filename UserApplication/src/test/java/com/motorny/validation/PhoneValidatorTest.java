package com.motorny.validation;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PhoneValidatorTest {

    @Test
    public void whenMatchesTwelveDigitsNumberPrefix_thenCorrect() {
        Pattern pattern = Pattern.compile("^\\+?\\d{1,3}[-.\\s]?\\d{3}[-.\\s]?\\d{3}[-.\\s]?\\d{3}$");
        Matcher matcher = pattern.matcher("+380951112233");

        assertTrue(matcher.matches());
    }
}