package testo.com.kis.loader;

import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.LoaderManager;
import com.kis.serializer.SerializerFactory;
import com.kis.utils.Connector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testo.com.kis.connection.MockConnection;
import testo.com.kis.domain.NodeCreator;

import java.io.IOException;
import java.sql.SQLException;

public class TestXmlLoader {

    @Before
    public void connect() {
        Connector.connect(MockConnection.getInstance().getConnection());
    }

    @Test
    public void testDatabaseLoader() throws SQLException {


        TreeNode database = LoaderManager.getRoot();
        for (TreeNode category : database.getChildren()) {
            for(TreeNode object : category.getChildren()) {
                LoaderManager.loadDetails(object);
            }
        }

        TreeNode expectation = NodeCreator.getInstance().getTreeNode();
        Assert.assertEquals(SerializerFactory.getXmlSerializer().serialize(database, null),
                SerializerFactory.getXmlSerializer().serialize(expectation, null));
        Assert.assertEquals(true, database.equalsTree(expectation));
    }

    @Test
    public void main() throws IOException {

    }
}
