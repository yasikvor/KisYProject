package com.kis.manager;

import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.LoaderManager;
import com.kis.loader.xmlloader.XmlLoader;
import com.kis.serializer.SerializerFactory;
import com.kis.utils.Connector;
import com.kis.utils.PropertyReader;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SerializeManager {

    private final ResourceBundle bundle = new PropertyReader("console/language").getBundle();

    private final String in = "-in";
    private final String out = "-out";
    private final String mode = "-mode";
    private final String tag = "-tag";
    private final String value = "-value";
    private final String text = "-text";
    private final String attribute = "-attr";
    private final String help = "-help";
    private final String type = "-nodeType";
    private final String user = "-user";
    private final String pass = "-pass";


    private static SerializeManager instance;


    private SerializeManager() {

    }

    public static SerializeManager getInstance() {
        if (instance == null) {
            instance = new SerializeManager();
        }
        return instance;
    }

    private String execute(Map<String, String> commands) throws SQLException {

        TreeNode treeNode;
        if (commands.get(type) == null || commands.get(type).toLowerCase().equals(bundle.getString("xml"))) {
            XmlLoader loader = new XmlLoader();
            if (!commands.get(in).isEmpty()) {
                treeNode = loader.load(new File(commands.get(in)));
                treeNode.setSerializer(SerializerFactory.getXmlSerializer());
            } else
                throw new NullPointerException();
        } else if (commands.get(type).toLowerCase().equals(bundle.getString("sql"))) {
            if (!commands.get(in).isEmpty() && !commands.get(user).isEmpty() && !commands.get(pass).isEmpty()) {
                Connector.connect(commands.get(in), commands.get(user), commands.get(pass));
                treeNode = LoaderManager.getRoot();
                treeNode.setSerializer(SerializerFactory.getSqlSerializer());
            } else
                throw new NullPointerException();
        } else
            throw new IllegalArgumentException();
        TreeNode root = new TreeNode("Root");
        root.setChildren(treeNode.search(commands.get(tag), commands.get(attribute), commands.get(value), commands.get(text), commands.get(mode), false));
        return root.serialize(new File(commands.get(out)));

    }

    /**
     * @param args - commands from console
     * @return content of the XML-tree in String format
     * @throws Exception
     */
    public String setCommands(String[] args) {

        if (args.length == 0)
            return null;

        if (args[0].equals(help)) {
            writeHelp();
            return null;
        }

        Map<String, String> commands = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            if (args[i] == null)
                break;
            if (args[i].equals(help)) {
                i++;
            }
            if (!args[i].equals(in) &&
                    !args[i].equals(out) &&
                    !args[i].equals(mode) &&
                    !args[i].equals(attribute) &&
                    !args[i].equals(value) &&
                    !args[i].equals(tag) &&
                    !args[i].equals(text) &&
                    !args[i].equals(help) &&
                    !args[i].equals(type) &&
                    !args[i].equals(user) &&
                    !args[i].equals(pass)) {
                writeHelp();
                throw new IllegalArgumentException(bundle.getString("UnknownCommand"));
            }

            commands.put(args[i], args[i + 1]);

        }
        try {
            return execute(commands);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeHelp() {
        System.out.println(bundle.getString("helpLabel"));
        System.out.println(in + bundle.getString("in"));
        System.out.println(out + bundle.getString("out"));
        System.out.println(tag + bundle.getString("tag"));
        System.out.println(value + bundle.getString("value"));
        System.out.println(text + bundle.getString("text"));
        System.out.println(attribute + bundle.getString("attribute"));
        System.out.println(mode + bundle.getString("mode"));
        System.out.println(help + bundle.getString("help"));
        System.out.println(type + bundle.getString("type"));
        System.out.println(user + bundle.getString("user"));
        System.out.println(pass + bundle.getString("pass"));
    }
}
