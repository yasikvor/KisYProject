package testo.com.kis.connection;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

class MockStatement {
    private static MockStatement instance;

    private MockStatement() {

    }

    static MockStatement getInstance() {
        if(instance == null)
            instance = new MockStatement();
        return instance;
    }

    PreparedStatement createStatementForDatabase() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(1);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.SPECIFIC_SCHEMA);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.SPECIFIC_SCHEMA);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForTables() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(3);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.TABLE_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.ENGINE);
        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.TABLE_COLLATION);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.TABLE_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.ENGINE);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.TABLE_COLLATION);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForTableColumns() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(5);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.COLUMN_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.COLUMN_TYPE);
        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.COLUMN_DEFAULT);
        Mockito.when(resultSet.getString(4)).thenReturn(AttributeNameConstants.IS_NULLABLE);
        Mockito.when(resultSet.getString(5)).thenReturn(AttributeNameConstants.EXTRA);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.COLUMN_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.COLUMN_TYPE);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.COLUMN_DEFAULT);
        Mockito.when(resultSet.getMetaData().getColumnName(4)).thenReturn(AttributeNameConstants.IS_NULLABLE);
        Mockito.when(resultSet.getMetaData().getColumnName(5)).thenReturn(AttributeNameConstants.EXTRA);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForIndexes() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(2);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn("PRIMARY").thenReturn("PRIMARY").thenReturn("uniq_UNIQUE").thenReturn("Something");
        Mockito.when(resultSet.getString(2)).thenReturn("PRIMARY").thenReturn("PRIMARY").thenReturn("uniq_UNIQUE").thenReturn("Someone");

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.COLUMN_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.INDEX_NAME);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForConstraints() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(4);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.CONSTRAINT_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.COLUMN_NAME);
        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.REFERENCED_TABLE_NAME);
        Mockito.when(resultSet.getString(4)).thenReturn(AttributeNameConstants.REFERENCED_COLUMN_NAME);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.CONSTRAINT_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.COLUMN_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.REFERENCED_TABLE_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(4)).thenReturn(AttributeNameConstants.REFERENCED_COLUMN_NAME);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForTriggers() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(5);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.TRIGGER_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.ACTION_ORIENTATION);
        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.ACTION_TIMING);
        Mockito.when(resultSet.getString(4)).thenReturn(AttributeNameConstants.ACTION_STATEMENT);
        Mockito.when(resultSet.getString(5)).thenReturn(AttributeNameConstants.EVENT_MANIPULATION);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.TRIGGER_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.ACTION_ORIENTATION);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.ACTION_TIMING);
        Mockito.when(resultSet.getMetaData().getColumnName(4)).thenReturn(AttributeNameConstants.ACTION_STATEMENT);
        Mockito.when(resultSet.getMetaData().getColumnName(5)).thenReturn(AttributeNameConstants.EVENT_MANIPULATION);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForViews() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(3);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.TABLE_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.DEFINER);
        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.VIEW_DEFINITION);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.TABLE_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.DEFINER);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.VIEW_DEFINITION);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForViewColumns() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(2);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.COLUMN_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.COLUMN_TYPE);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.COLUMN_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.COLUMN_TYPE);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForProcedures() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(4);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.ROUTINE_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.DEFINER);

        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.ROUTINE_DEFINITION);
        Mockito.when(resultSet.getString(4)).thenReturn(GrammarConstants.NodeNames.PROCEDURE.toUpperCase());

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.ROUTINE_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.DEFINER);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.ROUTINE_DEFINITION);
        Mockito.when(resultSet.getMetaData().getColumnName(4)).thenReturn(AttributeNameConstants.ROUTINE_TYPE);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForFunctions() throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(4);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.ROUTINE_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(AttributeNameConstants.DEFINER);

        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.ROUTINE_DEFINITION);
        Mockito.when(resultSet.getString(4)).thenReturn(GrammarConstants.NodeNames.FUNCTION.toUpperCase());

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.ROUTINE_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.DEFINER);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.ROUTINE_DEFINITION);
        Mockito.when(resultSet.getMetaData().getColumnName(4)).thenReturn(AttributeNameConstants.ROUTINE_TYPE);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        return statement;
    }

    PreparedStatement createStatementForParameters() throws SQLException {

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSetForFunction = this.getParams(null);
        ResultSet resultSetForProcedure = this.getParams(AttributeNameConstants.PARAMETER_MODE);
        Mockito.when(statement.executeQuery()).thenReturn(resultSetForProcedure).thenReturn(resultSetForFunction);

        return statement;
    }

    private ResultSet getParams(String paramMode) throws SQLException {

        ResultSet resultSet = Mockito.mock(ResultSet.class);

        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(2);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getString(1)).thenReturn(AttributeNameConstants.PARAMETER_NAME);
        Mockito.when(resultSet.getString(2)).thenReturn(paramMode);
        Mockito.when(resultSet.getString(3)).thenReturn(AttributeNameConstants.DTD_IDENTIFIER);

        Mockito.when(resultSet.getMetaData().getColumnName(1)).thenReturn(AttributeNameConstants.PARAMETER_NAME);
        Mockito.when(resultSet.getMetaData().getColumnName(2)).thenReturn(AttributeNameConstants.PARAMETER_MODE);
        Mockito.when(resultSet.getMetaData().getColumnName(3)).thenReturn(AttributeNameConstants.DTD_IDENTIFIER);

        return resultSet;
    }
}
