package com.kis.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class PropertyReader {

    private ResourceBundle bundle;

    public PropertyReader(String path) {
        bundle = ResourceBundle.getBundle(path, Locale.getDefault());
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

}