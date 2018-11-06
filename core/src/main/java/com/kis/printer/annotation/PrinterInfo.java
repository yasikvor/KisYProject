package com.kis.printer.annotation;

import java.lang.annotation.Retention;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface PrinterInfo {
    String type();
}
