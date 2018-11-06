package com.kis.viewmodel;

import com.kis.constant.SettingsConstant;
import com.kis.settings.Settings;
import javafx.beans.property.SimpleStringProperty;

public class SettingsViewModel {

    public SettingsViewModel() {
        projectPath.set(Settings.getProperty(SettingsConstant.PROJECT_PATH));
        schemaPath.set(Settings.getProperty(SettingsConstant.SCHEMA_PATH));
        logPath.set(Settings.getProperty(SettingsConstant.LOGGER_PATH));
    }

    private final SimpleStringProperty projectPath = new SimpleStringProperty();

    private final SimpleStringProperty schemaPath = new SimpleStringProperty();

    private final SimpleStringProperty logPath = new SimpleStringProperty();

    public SimpleStringProperty projectPathProperty() {
        return projectPath;
    }

    public SimpleStringProperty schemaPathProperty() {
        return schemaPath;
    }

    public SimpleStringProperty logPathProperty() {
        return logPath;
    }

    public void saveSetting() {
        Settings.setProperty(SettingsConstant.PROJECT_PATH, projectPath.getValue());
        Settings.setProperty(SettingsConstant.SCHEMA_PATH, schemaPath.getValue());
        Settings.setProperty(SettingsConstant.LOGGER_PATH, logPath.getValue());
        Settings.store();
    }

    public void closeWindow() {
        projectPath.set(Settings.getProperty(SettingsConstant.PROJECT_PATH));
        schemaPath.set(Settings.getProperty(SettingsConstant.SCHEMA_PATH));
        logPath.set(Settings.getProperty(SettingsConstant.LOGGER_PATH));
    }
}
