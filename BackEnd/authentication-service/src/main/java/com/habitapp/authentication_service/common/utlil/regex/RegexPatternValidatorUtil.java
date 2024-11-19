package com.menara.authentication.common.utlil.regex;

import com.menara.authentication.annotation.Util;

import java.util.regex.Pattern;

@Util
public class RegexPatternValidatorUtil {

    public boolean validateStringPattern(String string,String RegexPattern){
        return Pattern.compile(RegexPattern)
                .matcher(string)
                .matches();
    }
}
