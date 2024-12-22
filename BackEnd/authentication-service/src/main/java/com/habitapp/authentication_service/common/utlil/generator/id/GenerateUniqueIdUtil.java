package com.habitapp.authentication_service.common.utlil.generator.id;

import com.habitapp.authentication_service.annotation.Util;
import com.habitapp.authentication_service.common.utlil.converter.ConverterUtil;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Util
@AllArgsConstructor
public class GenerateUniqueIdUtil {
    private static long count = 0;
    private ConverterUtil converterUtil;

    public synchronized String generateUniqueId(){

        return converterUtil.bytesToHexadecimal((UUID.randomUUID().toString() + (Math.abs(++count))).getBytes());
    }
}
