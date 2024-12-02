package com.habitapp.reward_service.common.utlil.regex;


import java.util.regex.Pattern;

import com.habitapp.reward_service.annotation.Util;

@Util
public class RegexPatternValidatorUtil {

    public boolean validateStringPattern(String string,String RegexPattern){
        return Pattern.compile(RegexPattern)
                .matcher(string)
                .matches();
    }
}
