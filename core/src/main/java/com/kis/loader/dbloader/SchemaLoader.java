package com.kis.loader.dbloader;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.BatchQueryConstants;
import com.kis.constant.GrammarConstants;
import com.kis.constant.LazyQueryConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.annotation.LoaderInfo;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LoaderInfo(type = GrammarConstants.NodeNames.SCHEMA)
class SchemaLoader extends BaseLoader {

    private static final int NO_CATEGORY = -1;
    private static final int COLUMNS = 0;
    private static final int INDEXES = 1;
    private static final int CONSTRAINTS = 2;
    private static final int TRIGGERS = 3;


    private static Logger logger = Logger.getLogger(SchemaLoader.class.getName());

    @Override
    public TreeNode load() throws SQLException {

        logger.info("Database is loading...");
        ResultSet resultSet = loadStatement(LazyQueryConstants.SELECT_SCHEMA);
        resultSet.next();
        //FIXME
        TreeNode database = getTreeNode(resultSet, GrammarConstants.NodeNames.SCHEMA);
        final Map<String, Loader> loaders = LoaderManager.getLoaders();
        database.addChild(loaders.get(GrammarConstants.NodeNames.TABLE).load());
        database.addChild(loaders.get(GrammarConstants.NodeNames.VIEW).load());
        database.addChild(loaders.get(GrammarConstants.NodeNames.PROCEDURE).load());
        database.addChild(loaders.get(GrammarConstants.NodeNames.FUNCTION).load());
        logger.info("Database loaded");
        return database;


    }

    @Override
    public void loadDetails(TreeNode database) throws SQLException {
        database.addChild(loadAllTables());
        database.addChild(LoaderManager.fullLoad(GrammarConstants.NodeNames.VIEW));
        database.addChild(LoaderManager.fullLoad(GrammarConstants.NodeNames.PROCEDURE));
        database.addChild(LoaderManager.fullLoad(GrammarConstants.NodeNames.FUNCTION));
    }

    @Override
    public TreeNode reload(TreeNode treeNode) throws ObjectNotFoundException, SQLException {
        logger.info("Reloading database...");

        ResultSet resultSet = loadStatement(LazyQueryConstants.SELECT_SCHEMA);
        if (!resultSet.next()) {
            logger.info("Database has been deleted");
            throw new ObjectNotFoundException();
        }

        logger.info("Database reloaded");
        return load();
    }

    private TreeNode loadAllTables() throws SQLException {
        ResultSet resultSet = loadStatement(BatchQueryConstants.SELECT_TABLES);

        Map<String, TreeNode> tablesMap = new HashMap<>();

        while (resultSet.next()) {

            TreeNode table = getTreeNode(resultSet, GrammarConstants.NodeNames.TABLE);
            table.addChild(new TreeNode(GrammarConstants.CategoryNames.COLUMNS));
            table.addChild(new TreeNode(GrammarConstants.CategoryNames.INDEXES));
            table.addChild(new TreeNode(GrammarConstants.CategoryNames.CONSTRAINTS));
            table.addChild(new TreeNode(GrammarConstants.CategoryNames.TRIGGERS));
            tablesMap.put(table.toString(), table);
        }


        connectNodes(tablesMap, BatchQueryConstants.SELECT_TABLE_COLUMNS, GrammarConstants.NodeNames.COLUMN, COLUMNS);
        connectNodes(tablesMap, BatchQueryConstants.SELECT_INDEXES, GrammarConstants.NodeNames.KEY, INDEXES);
        connectNodes(tablesMap, BatchQueryConstants.SELECT_CONSTRAINTS, GrammarConstants.NodeNames.CONSTRAINT, CONSTRAINTS);
        connectNodes(tablesMap, BatchQueryConstants.SELECT_TRIGGERS, GrammarConstants.NodeNames.TRIGGER, TRIGGERS);

        TreeNode tables = new TreeNode(GrammarConstants.CategoryNames.TABLES);
        tables.setChildren(new ArrayList<>(tablesMap.values()));
        return tables;

    }

    private void connectNodes(Map<String, TreeNode> nodesMap, String query, String nodeType, int category) throws SQLException {
        String tableName;
        switch (nodeType) {
            case GrammarConstants.NodeNames.TRIGGER:
                tableName = AttributeNameConstants.EVENT_OBJECT_TABLE;
                break;
            case GrammarConstants.NodeNames.PARAMETER:
                tableName = AttributeNameConstants.SPECIFIC_NAME;
                break;
            default:
                tableName = AttributeNameConstants.TABLE_NAME;
        }

        List<TreeNode> treeNodes;
        switch (nodeType) {
            case GrammarConstants.NodeNames.KEY:
                treeNodes = loadAllIndexes();
                break;
            default:
                treeNodes = super.loadAllNodes(query, nodeType);
        }

        for (TreeNode treeNode : treeNodes) {
            if (nodesMap.get(treeNode.getAttribute(tableName)) != null) {
                nodesMap.get(treeNode.getAttribute(tableName)).addChild(treeNode);
                treeNode.getAttributes().remove(tableName);
            }
        }

    }

    private List<TreeNode> loadAllIndexes() throws SQLException {

        ResultSet resultSet = this.loadStatement(BatchQueryConstants.SELECT_INDEXES);

        List<TreeNode> indexes = new ArrayList<>();

        while (resultSet.next()) {
            Map<String, String> attributes = new HashMap<>();
            String indexName = resultSet.getString(2);
            attributes.put(AttributeNameConstants.NAME, resultSet.getString(1));
            attributes.put(AttributeNameConstants.INDEX_NAME, indexName);
            attributes.put(AttributeNameConstants.TABLE_NAME, resultSet.getString(3));
            TreeNode index = new TreeNode();
            index.setAttributes(attributes);
            if (indexName.toLowerCase().equals(GrammarConstants.NodeNames.PRIMARY.toLowerCase())) {
                index.setType(GrammarConstants.NodeNames.PRIMARY);
            } else if (indexName.toLowerCase().contains(GrammarConstants.NodeNames.UNIQUE.toLowerCase())) {
                index.setType(GrammarConstants.NodeNames.UNIQUE);
            } else {
                index.setType(GrammarConstants.NodeNames.KEY);
            }
            indexes.add(index);
        }
        return indexes;
    }

    @Override
    public TreeNode fullLoad() throws SQLException {
        return null;
    }
}
