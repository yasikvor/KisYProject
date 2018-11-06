package com.kis.constant;

public class LazyQueryConstants {

    public static final String SELECT_TRIGGERS = "select TRIGGER_NAME, ACTION_ORIENTATION, ACTION_TIMING, ACTION_STATEMENT, EVENT_MANIPULATION from information_schema.Triggers where EVENT_OBJECT_TABLE like ? AND TRIGGER_SCHEMA like ?";
    public static final String SELECT_TABLES = "select * from information_schema.TABLES where ENGINE IS NOT NULL AND TABLE_SCHEMA like ?";
    public static final String SELECT_TABLE_COLUMNS = "select COLUMN_NAME, COLUMN_TYPE, COLUMN_DEFAULT, IS_NULLABLE, EXTRA from information_schema.COLUMNS where TABLE_NAME like ? AND TABLE_SCHEMA like ?";
    public static final String SELECT_INDEXES = "select COLUMN_NAME , INDEX_NAME from information_schema.STATISTICS where TABLE_NAME like ? AND TABLE_SCHEMA like ?";
//    public static final String SELECT_INDEXES = "select * from information_schema.STATISTICS where TABLE_NAME like ? AND TABLE_SCHEMA like ?";
    public static final String SELECT_CONSTRAINTS = "select CONSTRAINT_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME from information_schema.KEY_COLUMN_USAGE where TABLE_NAME like ? AND REFERENCED_COLUMN_NAME IS NOT NULL AND TABLE_SCHEMA like ?";
    public static final String SELECT_VIEW_COLUMNS = "select COLUMN_NAME, COLUMN_TYPE from information_schema.COLUMNS where TABLE_NAME like ? AND TABLE_SCHEMA like ?";
    public static final String SELECT_VIEWS = "select TABLE_NAME, DEFINER, VIEW_DEFINITION from information_schema.VIEWS where TABLE_SCHEMA like ?";
    public static final String SELECT_PROCEDURE_PARAMETERS = "select PARAMETER_NAME, PARAMETER_MODE, DTD_IDENTIFIER from information_schema.PARAMETERS where SPECIFIC_NAME like ? AND SPECIFIC_SCHEMA like ?";
    public static final String SELECT_FUNCTION_PARAMETERS = "select PARAMETER_NAME, PARAMETER_MODE, DTD_IDENTIFIER from information_schema.PARAMETERS where SPECIFIC_NAME like ? AND SPECIFIC_SCHEMA like ? AND PARAMETER_NAME IS NOT NULL";

    public static final String SELECT_PROCEDURES = "select * FROM information_schema.ROUTINES where ROUTINE_SCHEMA like ? AND ROUTINE_TYPE like 'PROCEDURE'";
    public static final String SELECT_FUNCTIONS = "select * FROM information_schema.ROUTINES where ROUTINE_SCHEMA like ? AND ROUTINE_TYPE like 'FUNCTION'";
    public static final String SELECT_TABLES_FOR_REFRESH = "select TABLE_NAME from information_schema.TABLES where TABLE_NAME like ? and TABLE_SCHEMA like ?";
    public static final String SELECT_ROUTINES_FOR_REFRESH = "select ROUTINE_NAME from information_schema.ROUTINES where ROUTINE_NAME like ? and ROUTINE_SCHEMA like ? ";
    public static final String SELECT_SCHEMA = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME like ?";
}
