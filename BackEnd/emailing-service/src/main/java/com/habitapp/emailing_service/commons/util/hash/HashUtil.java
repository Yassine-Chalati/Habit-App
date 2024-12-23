package com.habitapp.emailing_service.commons.util.hash;

import com.habitapp.emailing_service.annotation.Util;
import com.habitapp.emailing_service.commons.util.converter.ConverterUtil;
import lombok.AllArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@AllArgsConstructor
@Util
public class HashUtil {
    private final ConverterUtil converterUtil;



    /**
     *
     * @param bytes {@summary take bytes in parameter}
     * @return {@return HashUtil code converted to String Hexadecimal}
     */
    public String hashWithSHA256AndReturnHexadecimalString(byte[] bytes){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(bytes);

            return this.converterUtil.bytesToHexadecimal(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param bytes {@summary take bytes in parameter}
     * @return {@return HashUtil code converted to String Hexadecimal}
     */
    public String hashWithMD5AndReturnHexadecimalString(byte[] bytes){
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] encodedHash = digest.digest(bytes);

            return this.converterUtil.bytesToHexadecimal(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
