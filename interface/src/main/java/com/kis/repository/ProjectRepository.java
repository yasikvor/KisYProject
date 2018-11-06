package com.kis.repository;

import com.kis.constant.ConnectionObjectConstant;
import com.kis.domain.TreeNode;
import com.kis.loader.xmlloader.XmlLoader;
import com.kis.model.ConnectionModel;
import com.kis.serializer.SerializerFactory;
import com.kis.service.TreeNodeService;
import com.kis.validator.XmlValidator;
import com.kis.view.TreeNodeView;
import javafx.scene.control.TreeItem;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

public class ProjectRepository {

    private static Logger logger = Logger.getLogger(TreeNodeView.class.getName());

    public static void save(TreeItem<TreeNode> tree, TreeItem<TreeNode> selectedItem, File file) {


        TreeNode root = new TreeNode("Project");

        root.addChild(saveConnection());

        saveExpanded(tree);
        saveSelected(selectedItem);
        root.addChild(tree.getValue());

        SerializerFactory.getXmlSerializer().serialize(root, file);
    }

    public static TreeItem<TreeNode> open(File file) throws SAXException, SQLException {

        if (ConnectionModel.getConnection() != null)
            ConnectionModel.close();

        logger.info("Opening file " + file.getName() + "...");
        XmlValidator.validate(file);
        TreeNode project = new XmlLoader().load(file);
        Map<String, String> connectionProperties = project.getChildren().get(0).getAttributes();
        ConnectionModel.setUrl(connectionProperties.get(ConnectionObjectConstant.URL));
        ConnectionModel.setUser(connectionProperties.get(ConnectionObjectConstant.USER));
        TreeNode root = project.getChildren().get(1);
        logger.info("File opened");
        return TreeNodeService.createRoot(root);
    }

    private static TreeNode saveConnection() {
        TreeNode connection = new TreeNode(ConnectionObjectConstant.CONNECTION);


//            DatabaseMetaData metaData = Connector.getConnection().getMetaData();
//            connection.addAttribute(ConnectionObjectConstant.URL, metaData.getURL());
//            connection.addAttribute(ConnectionObjectConstant.USER, metaData.getUserName().split("@")[0]);
        connection.addAttribute(ConnectionObjectConstant.URL, ConnectionModel.getUrl());
        connection.addAttribute(ConnectionObjectConstant.USER, ConnectionModel.getUser());

        return connection;
    }

    private static void saveSelected(TreeItem<TreeNode> selectedItem) {
        if (selectedItem != null)
            selectedItem.getValue().addAttribute(ConnectionObjectConstant.IS_SELECTED, "true");
    }

    private static void saveExpanded(TreeItem<TreeNode> tree) {
        if (tree.isExpanded())
            tree.getValue().addAttribute(ConnectionObjectConstant.IS_EXPANDED, "true");
        else
            tree.getValue().addAttribute(ConnectionObjectConstant.IS_EXPANDED, "false");
        if (tree.getChildren() == null || tree.getChildren().size() == 0)
            return;

        for (TreeItem<TreeNode> treeItem : tree.getChildren()) {
            saveExpanded(treeItem);
        }
    }
}
