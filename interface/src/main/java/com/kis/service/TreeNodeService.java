package com.kis.service;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.ConnectionObjectConstant;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.LoaderManager;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import com.kis.loader.dbloader.utils.DbObjectComparator;
import com.kis.model.ConnectionModel;
import com.kis.model.NodeModel;
import javafx.scene.control.TreeItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreeNodeService {

    static public void downloadNode(TreeNode node) {
        LoaderManager.loadDetails(node);
    }

    public static TreeItem<TreeNode> updateTreeNode(TreeItem<TreeNode> selectedItem) {
        selectedItem.getChildren().remove(0);
        createNode(selectedItem, selectedItem.getValue());
        return selectedItem;
    }

    static private void createNode(TreeItem<TreeNode> treeItem, TreeNode node) {

        if(node.getChildren().size() == 0)
            return;
        for(TreeNode childNode : node.getChildren()) {
            TreeItem<TreeNode> childTreeItem = new TreeItem<>(childNode);
            treeItem.getChildren().add(childTreeItem);
            createNode(childTreeItem, childNode);
        }
    }

    static public void resetFilter() {
        NodeModel.setRootTreeItem(createRootTreeItem(NodeModel.getRoot()));
    }

    static private void setExpandedAndSelectedToTree(TreeItem<TreeNode> treeItem) {

        if(treeItem.getValue().getAttributes().get(ConnectionObjectConstant.IS_EXPANDED) != null && treeItem.getValue().getAttributes().get(ConnectionObjectConstant.IS_EXPANDED).equals("true"))
            treeItem.setExpanded(true);

        if(treeItem.getValue().getAttributes().get(ConnectionObjectConstant.IS_SELECTED) != null) {
            NodeModel.setSelectedItem(treeItem);
            treeItem.getValue().getAttributes().remove(ConnectionObjectConstant.IS_SELECTED);
        }

        for(TreeItem<TreeNode> childItem : treeItem.getChildren()) {
            setExpandedAndSelectedToTree(childItem);
        }

    }

    static public void createRoot() {
        if(ConnectionModel.getConnection() == null)
            return;
        TreeNode root = NodeModel.getRoot();
        createTempChildNode(root);

        NodeModel.setRootTreeItem(createRootTreeItem(root));
    }

    public static TreeItem<TreeNode> createRoot(TreeNode root) {
        NodeModel.setRoot(root);
        TreeItem<TreeNode> tree = createRootTreeItem(root);
        setExpandedAndSelectedToTree(tree);
        return tree;
    }

    private static TreeItem<TreeNode> createRootTreeItem(TreeNode root) {
        TreeItem<TreeNode> rootTreeItem = new TreeItem<>(root);
        createNode(rootTreeItem, root);
        return rootTreeItem;
    }

    public static TreeItem<TreeNode> loadAllDatabase() throws SQLException{
        TreeNode node = new TreeNode(NodeModel.getRoot());
        LoaderManager.loadDetails(node);
        setLoaded(node);
        TreeItem<TreeNode> treeItem = new TreeItem<>(node);
        createNode(treeItem, node);
        NodeModel.setRoot(node);
        return treeItem;
    }

    private static void setLoaded(TreeNode database) {
        for(TreeNode category : database.getChildren()) {
            category.getChildren().forEach(node -> node.addAttribute(ConnectionObjectConstant.IS_LOADED, "true"));
        }
    }

    static private void createTempChildNode(TreeNode node) {
        for(TreeNode child : node.getChildren()) {
            if(DbObjectComparator.isDbObject(child)) {
                child.addChild(new TreeNode("Expanding..."));
                continue;
            }
            createTempChildNode(child);
        }
    }

    static public TreeItem<TreeNode> search(String name) {

        List<TreeNode> nodes = NodeModel.getRoot().search(null, AttributeNameConstants.NAME, name, null, null, false);
        TreeNode root;
        if(nodes.size() == 1)
            root = nodes.get(0);
        else {
            root = new TreeNode(NodeModel.getRoot());

            List<TreeNode> categories = new ArrayList<>();
            categories.add(new TreeNode(GrammarConstants.CategoryNames.TABLES));
            categories.add(new TreeNode(GrammarConstants.CategoryNames.VIEWS));
            categories.add(new TreeNode(GrammarConstants.CategoryNames.PROCEDURES));
            categories.add(new TreeNode(GrammarConstants.CategoryNames.FUNCTIONS));

            root.setChildren(categories);

            for (TreeNode child : nodes) {
                switch (child.getType()) {
                    case GrammarConstants.NodeNames.TABLE: {
                        categories.get(0).addChild(child);
                        break;
                    }
                    case GrammarConstants.NodeNames.VIEW: {
                        categories.get(1).addChild(child);
                        break;
                    }
                    case GrammarConstants.NodeNames.PROCEDURE: {
                        categories.get(2).addChild(child);
                        break;
                    }
                    case GrammarConstants.NodeNames.FUNCTION: {
                        categories.get(3).addChild(child);
                        break;
                    }
                }
            }
        }
        TreeItem<TreeNode> item = new TreeItem<>(root);
        createNode(item, root);
        return item;
    }

    static public boolean isDbObject(TreeNode node) {
        return DbObjectComparator.isDbObject(node);
    }

    static public void delete(TreeItem<TreeNode> treeItem) {
        treeItem.getParent().getChildren().remove(treeItem);
    }

    static public void reload(TreeItem<TreeNode> treeItem) throws ObjectNotFoundException, SQLException {
        if(ConnectionModel.getConnection() == null)
            throw new SQLException();
        TreeNode value = LoaderManager.reload(treeItem.getValue());
        treeItem.getChildren().removeAll(treeItem.getChildren());
        if(!DbObjectComparator.isDbObject(value)) {
            createTempChildNode(value);
        }
        createNode(treeItem, value);
        if(value.getType().equals(GrammarConstants.NodeNames.DATABASE)) {
            NodeModel.setRoot(value);
        }

    }
}
