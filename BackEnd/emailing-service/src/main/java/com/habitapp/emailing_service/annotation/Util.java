package com.internship_hiring_menara.emailing_service.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Util {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}