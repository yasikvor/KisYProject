package com.kis.loader.dbloader;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import com.kis.utils.Connector;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kis.constant.AttributeNameConstants.NAME;


abstract class BaseLoader implements Loader {

    private static Logger logger = Logger.getLogger(BaseLoader.class.getName());

    private static Map<String, String> nodeNames = new HashMap<>();

    static {
        nodeNames.put(GrammarConstants.NodeNames.TABLE, "TABLE_NAME");
        nodeNames.put(GrammarConstants.NodeNames.VIEW, "TABLE_NAME");
        nodeNames.put(GrammarConstants.NodeNames.PROCEDURE, "ROUTINE_NAME");
        nodeNames.put(GrammarConstants.NodeNames.FUNCTION, "ROUTINE_NAME");
        nodeNames.put(GrammarConstants.NodeNames.PARAMETER, "PARAMETER_NAME");
        nodeNames.put(GrammarConstants.NodeNames.COLUMN, "COLUMN_NAME");
        nodeNames.put(GrammarConstants.NodeNames.TRIGGER, "TRIGGER_NAME");
        nodeNames.put(GrammarConstants.NodeNames.CONSTRAINT, "CONSTRAINT_NAME");
        nodeNames.put(GrammarConstants.NodeNames.SCHEMA, "SCHEMA_NAME");
    }

    BaseLoader() {

    }

    /**
     * @param sql   - it'GrammarConstants a SQL-script, which are used for getting metadata from information_schema
     * @param label - the name of the routine, table or view
     * @return selected rows
     */
    ResultSet loadStatement(String sql, String label) throws SQLException {
        PreparedStatement statement = Connector.getConnection().prepareStatement(sql);
        if (label != null) {
            statement.setString(1, label);
            statement.setString(2, Connector.getConnection().getCatalog());
        } else
            statement.setString(1, Connector.getConnection().getCatalog());

        return statement.executeQuery();
    }
    
    ResultSet loadStatement(String sql) throws SQLException {
        return this.loadStatement(sql, null);
    }

    TreeNode getTreeNode(ResultSet resultSet, String nodeType) throws SQLException {
        return new TreeNode(nodeType, extractAttributes(resultSet, nodeType));
    }

    ArrayList<TreeNode> loadAllNodes(String query, String nodeType) throws SQLException {
        ResultSet resultSet = loadStatement(query);
        resultSet.last();
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            treeNodes.add(new TreeNode(nodeType, extractAttributes(resultSet, nodeType)));
        }
        resultSet.close();
        return treeNodes;

    }

    List<TreeNode> loadDetails(TreeNode node, String childNodeType, String sql) throws SQLException {
        final String nodeName = node.getAttribute(NAME);
        logger.info("Load details for " + node.getType() + " \"" + nodeName + "\"...");
        ResultSet resultSet = loadStatement(sql, nodeName);
        List<TreeNode> children = new ArrayList<>();
        while (resultSet.next()) {
            children.add(new TreeNode(childNodeType, this.extractAttributes(resultSet, childNodeType)));
        }
        logger.info("Details for view " + node.getType() + " \"" + nodeName + "\" loaded");
        resultSet.close();
        return children;
    }

    TreeNode load(String sql, String parentType, String childType) throws SQLException {
        logger.info("Loading " + parentType + "...");
        ResultSet resultSet = loadStatement(sql);
        TreeNode parentNode = new TreeNode(parentType);
        while (resultSet.next()) {
            TreeNode procedure = new TreeNode(childType, this.extractAttributes(resultSet, childType));
            parentNode.addChild(procedure);
        }
        logger.info(parentNode + " are loaded");
        resultSet.close();
        return parentNode;
    }

    TreeNode reload(String sql, TreeNode node) throws ObjectNotFoundException, SQLException {
        final String nodeName = node.getAttribute(NAME);
        logger.info("Reloading " + node.getType() + " \"" + nodeName + "\"...");

        ResultSet resultSet = loadStatement(sql, nodeName);
        if (!resultSet.next()) {
            throw new ObjectNotFoundException();
        }
        loadDetails(node);
        logger.info("View " + node.getType() + "\"" + nodeName + "\" reloaded");
        resultSet.close();
        return node;
    }

    private Map<String, String> extractAttributes(ResultSet resultSet, String nodeType) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String, String> attributes = new HashMap<>();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            String attr = metaData.getColumnName(i);
            String value = resultSet.getString(i);
            if (attr != null && value != null)
                attributes.put(attr, value);
        }
        String nodeName = attributes.remove(nodeNames.get(nodeType));
        attributes.put(NAME, nodeName);
        return attributes;
    }

    TreeNode fullLoad(String batchQuery, String nodeType, String attributeName) throws SQLException {
        TreeNode category = this.load();
        List<TreeNode> children = loadAllNodes(batchQuery, nodeType);

        for (TreeNode view : category.getChildren()) {
            String name = view.getAttribute(AttributeNameConstants.NAME);
            view.setChildren(children.stream()
                    .filter(parameter -> parameter.getAttribute(attributeName).equals(name))
                    .collect(Collectors.toList()));
        }
        return category;
    }
}
