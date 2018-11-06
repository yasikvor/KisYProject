package testo.com.kis.serializer;

import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.LoaderManager;
import com.kis.serializer.SerializerFactory;
import com.kis.utils.Connector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;
import testo.com.kis.connection.MockConnection;
import testo.com.kis.domain.NodeCreator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;


public class TestSqlSerializer {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void connect() {
        Connector.connect(MockConnection.getInstance().getConnection());
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testMockDBConnection() throws Exception {

        TreeNode treeNode = LoaderManager.getRoot();

        SerializerFactory.getXmlSerializer().serialize(treeNode, null);
    }

    @Test
    public void caseSendNullFileToSerializer() throws SQLException {
        TreeNode testTreeNode = NodeCreator.getInstance().getTreeNode();
        NodeCreator.getInstance().getDetails(testTreeNode);
        String expectation =
                "DROP TABLE IF EXISTS `TABLE_NAME`;" + System.lineSeparator() +
                        "CREATE TABLE `TABLE_NAME` (" + System.lineSeparator() +
                        "\t`COLUMN_NAME` COLUMN_TYPE DEFAULT `COLUMN_DEFAULT` EXTRA," + System.lineSeparator() +
                        "\tPRIMARY KEY (`PRIMARY`,`PRIMARY`)," + System.lineSeparator() +
                        "\tUNIQUE KEY `uniq_UNIQUE` (`uniq_UNIQUE`),"+ System.lineSeparator() +
                        "\tKEY `Someone` (`Something`)," + System.lineSeparator() +
                        "\tCONSTRAINT `CONSTRAINT_NAME` FOREIGN KEY (`COLUMN_NAME`) REFERENCES `REFERENCED_TABLE_NAME` (`REFERENCED_COLUMN_NAME`)," + System.lineSeparator() +
                        "CREATE TRIGGER `TRIGGER_NAME` ACTION_TIMING EVENT_MANIPULATION ON `TABLE_NAME` FOR EACH ACTION_ORIENTATION ACTION_STATEMENT;"+ System.lineSeparator() +
                        System.lineSeparator() +
                        ") ENGINE=ENGINE DEFAULT CHARSET=TABLE_COLLATION;" + System.lineSeparator() +
                        System.lineSeparator() +
                        "DROP TABLE IF EXISTS `TABLE_NAME`;" + System.lineSeparator() +
                        "CREATE VIEW `TABLE_NAME` AS VIEW_DEFINITION;" + System.lineSeparator() +
                        System.lineSeparator() +
                        "CREATE DEFINER = `DEFINER` PROCEDURE `ROUTINE_NAME` (PARAMETER_MODE PARAMETER_NAME DTD_IDENTIFIER )" + System.lineSeparator() +
                        "ROUTINE_DEFINITION;" + System.lineSeparator() +
                        System.lineSeparator() +
                        "CREATE DEFINER = `DEFINER` FUNCTION `ROUTINE_NAME`() RETURNS DTD_IDENTIFIER" + System.lineSeparator() +
                        "ROUTINE_DEFINITION;" + System.lineSeparator() + System.lineSeparator();

        Assert.assertEquals(SerializerFactory.getXmlSerializer().serialize(testTreeNode, null), expectation);
    }

    @Test
    public void caseSendFileWithWrongNameToSerializer() {
        thrown.expect(IllegalArgumentException.class);
        SerializerFactory.getXmlSerializer().serialize(new TreeNode("Name"), new File("123"));
    }

    @Test
    public void caseSendNormalValuesToSerializer() throws SQLException {

        File file = new File(this.getClass().getClassLoader().getResource("com/kis/serializer/test.sql").getFile());
        String expectation =
                "DROP TABLE IF EXISTS `TABLE_NAME`;" + System.lineSeparator() +
                        "CREATE TABLE `TABLE_NAME` (" + System.lineSeparator() +
                        "\t`COLUMN_NAME` COLUMN_TYPE DEFAULT `COLUMN_DEFAULT` EXTRA," + System.lineSeparator() +
                        "\tPRIMARY KEY (`PRIMARY`,`PRIMARY`)," + System.lineSeparator() +
                        "\tUNIQUE KEY `uniq_UNIQUE` (`uniq_UNIQUE`),"+ System.lineSeparator() +
                        "\tKEY `Someone` (`Something`)," + System.lineSeparator() +
                        "\tCONSTRAINT `CONSTRAINT_NAME` FOREIGN KEY (`COLUMN_NAME`) REFERENCES `REFERENCED_TABLE_NAME` (`REFERENCED_COLUMN_NAME`)," + System.lineSeparator() +
                        "CREATE TRIGGER `TRIGGER_NAME` ACTION_TIMING EVENT_MANIPULATION ON `TABLE_NAME` FOR EACH ACTION_ORIENTATION ACTION_STATEMENT;"+ System.lineSeparator() +
                        System.lineSeparator() +
                        ") ENGINE=ENGINE DEFAULT CHARSET=TABLE_COLLATION;" + System.lineSeparator() +
                        System.lineSeparator() +
                        "DROP TABLE IF EXISTS `TABLE_NAME`;" + System.lineSeparator() +
                        "CREATE VIEW `TABLE_NAME` AS VIEW_DEFINITION;" + System.lineSeparator() +
                        System.lineSeparator() +
                        "CREATE DEFINER = `DEFINER` PROCEDURE `ROUTINE_NAME` (PARAMETER_MODE PARAMETER_NAME DTD_IDENTIFIER )" + System.lineSeparator() +
                        "ROUTINE_DEFINITION;" + System.lineSeparator() +
                        System.lineSeparator() +
                        "CREATE DEFINER = `DEFINER` FUNCTION `ROUTINE_NAME`() RETURNS DTD_IDENTIFIER" + System.lineSeparator() +
                        "ROUTINE_DEFINITION;" + System.lineSeparator() + System.lineSeparator();
        TreeNode testTreeNode = NodeCreator.getInstance().getTreeNode();
        NodeCreator.getInstance().getDetails(testTreeNode);
        SerializerFactory.getXmlSerializer().serialize(testTreeNode, file);
        String fromFile = null;
        try {
            StringBuilder script = new StringBuilder();
            FileReader reader = new FileReader(file);
            int c;
            while((c = reader.read()) != -1){
                script.append((char)c);
            }
            fromFile = script.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(expectation, fromFile);
    }

    @Test
    public void caseSendNormalValuesToDeserializer() throws Exception {

        TreeNode testTreeNode = NodeCreator.getInstance().getTreeNode();

        SerializerFactory.getXmlSerializer().serialize(testTreeNode, null);
        TreeNode database = LoaderManager.getRoot();
        Assert.assertEquals(database.equals(testTreeNode), true);

    }
}
