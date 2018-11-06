package com.kis.connection;

import com.kis.constant.ConnectionObjectConstant;
import com.kis.constant.SettingsConstant;
import com.kis.domain.TreeNode;
import com.kis.loader.xmlloader.XmlLoader;
import com.kis.serializer.SerializerFactory;
import com.kis.settings.Settings;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConnectionRepository {

    private static Map<String, TreeNode> nodes;

    private static File connectionFile = new File(Settings.getProperty(SettingsConstant.CONNECTION_PATH));

    public static void loadRoot() {

        if (connectionFile.length() == 0)
            createFile(connectionFile);

        nodes = new HashMap<>();
        nodes.put(ConnectionObjectConstant.ROOT, new XmlLoader().load(connectionFile));
    }

    public static void rememberNode(TreeNode node) {
        nodes.put(node.getType(), node);
    }

    private static void createFile(File file) {
        TreeNode node = new TreeNode(ConnectionObjectConstant.ROOT);
        SerializerFactory.getXmlSerializer().serialize(node, file);
    }

    public static TreeNode getRoot() {
        return nodes.get(ConnectionObjectConstant.ROOT);

    }

    private static void connectTreeNodes() {
        nodes.put(ConnectionObjectConstant.SERVER, new TreeNode(nodes.get(ConnectionObjectConstant.SERVER)));
        nodes.put(ConnectionObjectConstant.PORT, new TreeNode(nodes.get(ConnectionObjectConstant.PORT)));
        nodes.put(ConnectionObjectConstant.SCHEMA, new TreeNode(nodes.get(ConnectionObjectConstant.SCHEMA)));
        nodes.put(ConnectionObjectConstant.USER, new TreeNode(nodes.get(ConnectionObjectConstant.USER)));

        //nodes.get(ConnectionObjectConstant.ROOT).addChild(nodes.get(ConnectionObjectConstant.SERVER));
        nodes.get(ConnectionObjectConstant.SERVER).addChild(nodes.get(ConnectionObjectConstant.PORT));
        nodes.get(ConnectionObjectConstant.PORT).addChild(nodes.get(ConnectionObjectConstant.SCHEMA));
        nodes.get(ConnectionObjectConstant.SCHEMA).addChild(nodes.get(ConnectionObjectConstant.USER));
    }

    public static void saveConnection() {
        connectTreeNodes();
        TreeNode root = nodes.get(ConnectionObjectConstant.ROOT);
        mergeBranches(root);
        SerializerFactory.getXmlSerializer().serialize(root, connectionFile);
    }

    private static void mergeBranches(TreeNode root) {
        if (root.getChildren().size() == 0) {
            root.addChild(nodes.get(ConnectionObjectConstant.SERVER));
            return;
        }
        addTreeNode(root);
    }

    private static void addTreeNode(TreeNode parent) {
        if (parent.getChildren().size() == 0)
            return;

        boolean isFound = false;

        for (TreeNode node : parent.getChildren()) {
            if (node.equals(nodes.get(node.getType()))) {
                isFound = true;
                addTreeNode(node);
                break;
            }

        }

        if (!isFound)
            parent.addChild(nodes.get(parent.getChildren().get(0).getType()));
    }

    public static TreeNode getNode(String type) {
        return nodes.get(type);
    }
}

