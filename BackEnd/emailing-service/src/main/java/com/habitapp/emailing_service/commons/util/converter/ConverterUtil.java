package com.habitapp.emailing_service.commons.util.converter;


import com.habitapp.emailing_service.annotation.Util;

@Util
public class ConverterUtil {

    /**
     *
     * @param bytes {@summary take bytes in parameter}
     * @return {@return an String Hexadecimal of the argument bytes}
     */
    public String bytesToHexadecimal(byte[] bytes){
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for(byte b : bytes){
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
