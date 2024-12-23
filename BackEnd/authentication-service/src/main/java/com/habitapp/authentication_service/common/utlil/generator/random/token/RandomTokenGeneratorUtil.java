package com.habitapp.authentication_service.common.utlil.generator.random.token;


import com.habitapp.authentication_service.annotation.Util;
import com.habitapp.authentication_service.common.utlil.cryptography.hash.HashUtil;
import lombok.AllArgsConstructor;

import java.security.SecureRandom;

@Util
@AllArgsConstructor
public class RandomTokenGeneratorUtil {
    private final HashUtil hashUtil;


    /**
     * generate a random token that contains 64 characters.
     * it generates a random 64 bytes
     * then hash this random 64 bytes with SHA256
     * it returns an arrays of 512 bytes
     * then convert this 512 bytes to a string of hexadecimal characters
     * @return random token of 64 hexadecimal characters
     */
    public String generateRandomToken64Characters(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = secureRandom.generateSeed(64);

        return hashUtil.hashWithSHA256AndReturnHexadecimalString(randomBytes);
    }

    public String generateRandomToken32Characters(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = secureRandom.generateSeed(64);

        return hashUtil.hashWithMD5AndReturnHexadecimalString(randomBytes);
    }
}
