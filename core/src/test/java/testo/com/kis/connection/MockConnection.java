package testo.com.kis.connection;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.LazyQueryConstants;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MockConnection {

    private static MockConnection instance;

    private MockConnection() {

    }

    private Connection connection;

    public static MockConnection getInstance() {
        if(instance == null) {
            instance = new MockConnection();
        }
        return instance;
    }

    private Connection createConnection() {
        Connection jdbcConnection = Mockito.mock(Connection.class);
        try {
            Mockito.when(jdbcConnection.getCatalog()).thenReturn(AttributeNameConstants.SPECIFIC_SCHEMA);

            PreparedStatement statementForDatabase = MockStatement.getInstance().createStatementForDatabase();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_SCHEMA)).thenReturn(statementForDatabase);

            PreparedStatement statementForTables = MockStatement.getInstance().createStatementForTables();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_TABLES)).thenReturn(statementForTables);

            PreparedStatement statementForTableColumns = MockStatement.getInstance().createStatementForTableColumns();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_TABLE_COLUMNS)).thenReturn(statementForTableColumns);

            PreparedStatement statementForIndexes = MockStatement.getInstance().createStatementForIndexes();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_INDEXES)).thenReturn(statementForIndexes);

            PreparedStatement statementForConstraints = MockStatement.getInstance().createStatementForConstraints();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_CONSTRAINTS)).thenReturn(statementForConstraints);

            PreparedStatement statementForTriggers = MockStatement.getInstance().createStatementForTriggers();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_TRIGGERS)).thenReturn(statementForTriggers);

            PreparedStatement statementForViews = MockStatement.getInstance().createStatementForViews();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_VIEWS)).thenReturn(statementForViews);

            PreparedStatement statementForViewColumns = MockStatement.getInstance().createStatementForViewColumns();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_VIEW_COLUMNS)).thenReturn(statementForViewColumns);

            PreparedStatement statementForViewProcedures = MockStatement.getInstance().createStatementForProcedures();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_PROCEDURES)).thenReturn(statementForViewProcedures);

            PreparedStatement statementForViewFunctions = MockStatement.getInstance().createStatementForFunctions();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_FUNCTIONS)).thenReturn(statementForViewFunctions);

            PreparedStatement statementForViewParameters = MockStatement.getInstance().createStatementForParameters();
            Mockito.when(jdbcConnection.prepareStatement(LazyQueryConstants.SELECT_PROCEDURE_PARAMETERS)).thenReturn(statementForViewParameters);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jdbcConnection;
    }

    public Connection getConnection() {
        if(connection == null) {
            connection = this.createConnection();
        }
        return connection;
    }
}
