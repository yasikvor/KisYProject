package com.kis.constant;

public class BatchQueryConstants {
    public static final String SELECT_TRIGGERS = "select TRIGGER_NAME, ACTION_ORIENTATION, ACTION_TIMING, ACTION_STATEMENT, EVENT_MANIPULATION, EVENT_OBJECT_TABLE from information_schema.Triggers where TRIGGER_SCHEMA like ?";
    public static final String SELECT_TABLES = "select TABLE_NAME, ENGINE, TABLE_COLLATION from information_schema.TABLES where ENGINE IS NOT NULL AND TABLE_SCHEMA like ?";
    public static final String SELECT_TABLE_COLUMNS = "select COLUMN_NAME, COLUMN_TYPE, COLUMN_DEFAULT, IS_NULLABLE, EXTRA, TABLE_NAME from information_schema.COLUMNS where TABLE_SCHEMA like ?";
    public static final String SELECT_INDEXES = "select COLUMN_NAME , INDEX_NAME, TABLE_NAME from information_schema.STATISTICS where TABLE_SCHEMA like ?";
    public static final String SELECT_CONSTRAINTS = "select * from information_schema.KEY_COLUMN_USAGE where REFERENCED_COLUMN_NAME IS NOT NULL AND TABLE_SCHEMA like ?";
    public static final String SELECT_VIEW_COLUMNS = "select * from information_schema.COLUMNS where TABLE_SCHEMA like ?";
    public static final String SELECT_PROCEDURE_PARAMETERS = "select * from information_schema.PARAMETERS where SPECIFIC_SCHEMA like ?";
    public static final String SELECT_FUNCTION_PARAMETERS = "select * from information_schema.PARAMETERS where PARAMETER_NAME IS NOT NULL AND SPECIFIC_SCHEMA like ?";
}
