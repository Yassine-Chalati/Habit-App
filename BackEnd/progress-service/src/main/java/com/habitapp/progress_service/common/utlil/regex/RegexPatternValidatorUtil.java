package com.habitapp.progress_service.common.utlil.regex;

import java.util.regex.Pattern;

import com.habitapp.progress_service.annotation.Util;

@Util
public class RegexPatternValidatorUtil {

    public boolean validateStringPattern(String string, String RegexPattern) {
        return Pattern.compile(RegexPattern)
                .matcher(string)
                .matches();
    }
}
