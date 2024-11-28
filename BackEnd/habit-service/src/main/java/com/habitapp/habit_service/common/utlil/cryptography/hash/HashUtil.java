package com.habitapp.habit_service.common.utlil.cryptography.hash;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.habitapp.habit_service.annotation.Util;
import com.habitapp.habit_service.common.utlil.converter.ConverterUtil;

import lombok.AllArgsConstructor;

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
