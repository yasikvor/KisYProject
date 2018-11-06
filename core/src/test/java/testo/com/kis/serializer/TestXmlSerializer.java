package testo.com.kis.serializer;

import com.kis.domain.TreeNode;
import com.kis.loader.xmlloader.XmlLoader;
import com.kis.serializer.Serializer;
import com.kis.serializer.SerializerFactory;
import com.kis.utils.Connector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import testo.com.kis.connection.MockConnection;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class TestXmlSerializer {

    @Before
    public void connect() {
        Connector.connect(MockConnection.getInstance().getConnection());
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void caseSendNullFileToSerializer() throws Exception {
        TreeNode parent = new TreeNode("Parent");
        Map<String, String> map = new HashMap<>();
        map.put("name", "Alex");
        map.put("String", "Yasik");
        TreeNode c1 = new TreeNode("child_", map);

        TreeNode c11 = new TreeNode("child__", map);
        c1.addChild(c11);

        TreeNode c111 = new TreeNode("child___", map);
        c11.addChild(c111);

        TreeNode child = new TreeNode("child____", map);
        c111.addChild(child);
        child.setText("Text");
        parent.addChild(c1);
        Assert.assertEquals(SerializerFactory.getXmlSerializer().serialize(parent, null),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<Parent>\r\n" +
                        "  <child_ name=\"Alex\" String=\"Yasik\">\r\n" +
                        "    <child__ name=\"Alex\" String=\"Yasik\">\r\n" +
                        "      <child___ name=\"Alex\" String=\"Yasik\">\r\n" +
                        "        <child____ name=\"Alex\" String=\"Yasik\"><![CDATA[Text]]></child____>\r\n" +
                        "      </child___>\r\n" +
                        "    </child__>\r\n" +
                        "  </child_>\r\n" +
                        "</Parent>\r\n\r\n");
    }

    @Test
    public void caseSendFileWithWrongNameToSerializer() {
        thrown.expect(IllegalArgumentException.class);
        SerializerFactory.getXmlSerializer().serialize(new TreeNode("Name"), new File("123"));
    }

    @Test
    public void caseSendFileWithWrongNameToDeserializer() throws ParserConfigurationException {
        thrown.expect(IllegalArgumentException.class);
        XmlLoader loader = new XmlLoader();
        loader.load(new File("123"));
    }


    @Test
    public void caseSendNormalValuesToSerializer() throws Exception {
        TreeNode parent = new TreeNode("Parent");
        Map<String, String> map = new HashMap<>();
        map.put("name", "Alex");
        map.put("String", "Yasik");
        TreeNode c1 = new TreeNode("child_", map);

        TreeNode c11 = new TreeNode("child__", map);
        c1.addChild(c11);

        TreeNode c111 = new TreeNode("child___", map);
        c11.addChild(c111);

        TreeNode child = new TreeNode("child____", map);
        c111.addChild(child);
        child.setText("Text");

        parent.addChild(c1);
        Assert.assertEquals(SerializerFactory.getXmlSerializer().serialize(parent, (new File(this.getClass().getClassLoader().getResource("com/kis/serializer/ConfigTests.xml").getFile()))),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<Parent>\r\n" +
                        "  <child_ name=\"Alex\" String=\"Yasik\">\r\n" +
                        "    <child__ name=\"Alex\" String=\"Yasik\">\r\n" +
                        "      <child___ name=\"Alex\" String=\"Yasik\">\r\n" +
                        "        <child____ name=\"Alex\" String=\"Yasik\"><![CDATA[Text]]></child____>\r\n" +
                        "      </child___>\r\n" +
                        "    </child__>\r\n" +
                        "  </child_>\r\n" +
                        "</Parent>\r\n\r\n");

    }

    @Test
    public void caseSendNormalValuesToDeserializer() throws Exception {
        TreeNode parent = new TreeNode("Parent");
        Map<String, String> map = new HashMap<>();
        map.put("name", "Alex");
        map.put("String", "Yasik");
        TreeNode c1 = new TreeNode("child_", map);

        TreeNode c11 = new TreeNode("child__", map);
        c1.addChild(c11);

        TreeNode c111 = new TreeNode("child___", map);
        c11.addChild(c111);



        TreeNode child = new TreeNode("child____", map);
        c111.addChild(child);
        child.setText("Text");
        parent.addChild(c1);
        XmlLoader loader = new XmlLoader();
        TreeNode treeNode = loader.load(new File(this.getClass().getClassLoader().getResource("com/kis/serializer/ConfigTests.xml").getFile()));


        Assert.assertEquals(true, treeNode.equalsTree(parent));
    }
}
