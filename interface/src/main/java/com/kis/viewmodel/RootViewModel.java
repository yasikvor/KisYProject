package com.kis.viewmodel;

import javafx.beans.property.SimpleBooleanProperty;

public class RootViewModel {

    private final SimpleBooleanProperty newProjectDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty openProjectDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty saveProjectDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty closeProjectDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty connectDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty loadAllDatabaseDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty reloadDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty createSqlDisabled = new SimpleBooleanProperty();

    public SimpleBooleanProperty newProjectDisabledProperty() {
        return newProjectDisabled;
    }

    public SimpleBooleanProperty openProjectDisabledProperty() {
        return openProjectDisabled;
    }

    public SimpleBooleanProperty saveProjectDisabledProperty() {
        return saveProjectDisabled;
    }

    public SimpleBooleanProperty closeProjectDisabledProperty() {
        return closeProjectDisabled;
    }

    public SimpleBooleanProperty connectDisabledProperty() {
        return connectDisabled;
    }


    public SimpleBooleanProperty loadAllDatabaseDisabledProperty() {
        return loadAllDatabaseDisabled;
    }


    public SimpleBooleanProperty reloadDisabledProperty() {
        return reloadDisabled;
    }

    public SimpleBooleanProperty createSqlDisabledProperty() {
        return createSqlDisabled;
    }
}
