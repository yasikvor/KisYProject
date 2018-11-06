package com.kis.loader.dbloader;

import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.annotation.LoaderInfo;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import com.kis.loader.dbloader.utils.DbObjectComparator;
import com.kis.utils.Connector;
import com.kis.utils.ReflectionUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoaderManager {

    private static Logger logger = Logger.getLogger(LoaderManager.class.getName());

    private static Map<String, Loader> loaders = new HashMap<>();

    private static Connection currentConnection;

    private static TreeNode root;

    static {
        registerLoaders();
    }

    private static TreeNode loadTopLevel() throws SQLException {
        try {
            return loaders.get(GrammarConstants.NodeNames.SCHEMA).load();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static TreeNode reloadCategory(TreeNode treeNode) throws SQLException {

        try {
            switch (treeNode.getType()) {
                case GrammarConstants.CategoryNames.TABLES: {
                    return loaders.get(GrammarConstants.NodeNames.TABLE).load();
                }
                case GrammarConstants.CategoryNames.VIEWS: {
                    return loaders.get(GrammarConstants.NodeNames.VIEW).load();
                }
                case GrammarConstants.CategoryNames.PROCEDURES: {
                    return loaders.get(GrammarConstants.NodeNames.PROCEDURE).load();
                }
                case GrammarConstants.CategoryNames.FUNCTIONS: {
                    return loaders.get(GrammarConstants.NodeNames.FUNCTION).load();
                }
                default:
                    return null;
            }
        } catch (SQLException e) {
            logger.trace(e);
            throw e;
        }
    }

    public static void loadDetails(TreeNode treeNode) {
        if (loaders.get(treeNode.getType()) == null)
            return;
        try {
            loaders.get(treeNode.getType()).loadDetails(treeNode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TreeNode reload(TreeNode treeNode) throws ObjectNotFoundException, SQLException {
        try {
            if (DbObjectComparator.isCategory(treeNode))
                return reloadCategory(treeNode);
            else {
                return loaders.get(treeNode.getType()).reload(treeNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void registerLoaders() {
        logger.info("Register lazy loaders...");
        loaders = ReflectionUtils.getAnnotatedClasses(LoaderInfo.class);
        logger.info("Loaders are registered");

    }

    public static TreeNode getRoot() throws SQLException {
        if (currentConnection != Connector.getConnection())
            root = null;
        if (root == null) {
            currentConnection = Connector.getConnection();
            root = loadTopLevel();
        }
        return root;
    }

    static Map<String, Loader> getLoaders() {
        return loaders;
    }

    static TreeNode fullLoad(String nodeType) throws SQLException {
        return loaders.get(nodeType).fullLoad();
    }
}
