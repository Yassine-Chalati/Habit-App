package com.habitapp.reward_service.common.utlil.generator.id;

import java.util.UUID;

import com.habitapp.reward_service.annotation.Util;
import com.habitapp.reward_service.common.utlil.converter.ConverterUtil;

import lombok.AllArgsConstructor;

@Util
@AllArgsConstructor
public class GenerateUniqueIdUtil {
    private static long count = 0;
    private ConverterUtil converterUtil;

    public synchronized String generateUniqueId(){

        return converterUtil.bytesToHexadecimal((UUID.randomUUID().toString() + (Math.abs(++count))).getBytes());
    }
}
