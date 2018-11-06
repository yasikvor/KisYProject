package testo.com.kis.printer;

import com.kis.domain.TreeNode;
import com.kis.printer.PrinterManager;
import com.kis.utils.Connector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testo.com.kis.connection.MockConnection;
import testo.com.kis.domain.NodeCreator;

public class TestPrinter {

    @Before
    public void connect() {
        Connector.connect(MockConnection.getInstance().getConnection());
    }

    @Test
    public void testPrintNode() {
        TreeNode database = NodeCreator.getInstance().getTreeNode();
        StringBuilder mainScript = new StringBuilder();
        for (TreeNode categories : database.getChildren()) {
            for (TreeNode treeNode : categories.getChildren()) {
                mainScript.append(PrinterManager.print(treeNode));
            }
        }

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

        //System.out.println(expectation);
        //System.out.println(mainScript.toString());
        Assert.assertEquals(expectation, mainScript.toString());
    }
}
