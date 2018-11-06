package com.kis.loader.annotation;

import java.lang.annotation.Retention;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface LoaderInfo {
    String type();
}
