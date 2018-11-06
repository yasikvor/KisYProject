package com.kis.model;

import com.kis.domain.TreeNode;

import javafx.scene.control.TreeItem;

public class NodeModel {

    private static TreeNode root;
    private static TreeItem<TreeNode> rootTreeItem;
    private static TreeItem<TreeNode> selectedItem;

    public static TreeNode getRoot() {
        return root;
    }

    public static void setRoot(TreeNode root) {
        NodeModel.root = root;
    }

    public static TreeItem<TreeNode> getRootTreeItem() {
        return rootTreeItem;
    }

    public static void setRootTreeItem(TreeItem<TreeNode> rootTreeItem) {
        NodeModel.rootTreeItem = rootTreeItem;
    }

    public static TreeItem<TreeNode> getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(TreeItem<TreeNode> selectedItem) {
        NodeModel.selectedItem = selectedItem;
    }

    public static void close() {
        root = null;
        rootTreeItem = null;
        selectedItem = null;
    }
}
