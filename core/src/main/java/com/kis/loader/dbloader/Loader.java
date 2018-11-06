package com.kis.loader.dbloader;

import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;

import java.sql.SQLException;

public interface Loader {

    /**
     * Method for getting nodes from DB
     */
    TreeNode load() throws SQLException;

    /**
     * Method for reloading nodes from DB
     */
    TreeNode reload(TreeNode treeNode) throws ObjectNotFoundException, SQLException;

    /**
     * Method for getting details for nodes from DB
     *
     * @param treeNode - parent node
     */
    void loadDetails(TreeNode treeNode) throws SQLException;

    /**
     * Method for getting nodes with all children from DB
     */
    TreeNode fullLoad() throws SQLException;
}
