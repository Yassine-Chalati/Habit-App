package com.habitapp.profile_service.common.utlil.regex;


import com.habitapp.profile_service.annotation.Util;

import java.util.regex.Pattern;

@Util
public class RegexPatternValidatorUtil {

    public boolean validateStringPattern(String string,String RegexPattern){
        return Pattern.compile(RegexPattern)
                .matcher(string)
                .matches();
    }
}
