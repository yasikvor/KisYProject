package com.kis.loader.dbloader;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import com.kis.constant.LazyQueryConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.annotation.LoaderInfo;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.kis.constant.AttributeNameConstants.NAME;
import static com.kis.constant.GrammarConstants.NodeNames.*;
import static com.kis.constant.LazyQueryConstants.*;

@LoaderInfo(type = GrammarConstants.NodeNames.TABLE)
class TableLoader extends BaseLoader {

    private static final int COLUMNS = 0;
    private static final int INDEXES = 1;
    private static final int CONSTRAINTS = 2;
    private static final int TRIGGERS = 3;

    private static Logger logger = Logger.getLogger(TableLoader.class.getName());

    @Override
    public TreeNode load() throws SQLException {
        return load(
                LazyQueryConstants.SELECT_TABLES,
                GrammarConstants.CategoryNames.TABLES,
                GrammarConstants.NodeNames.TABLE);
    }

    @Override
    public TreeNode reload(TreeNode table) throws ObjectNotFoundException, SQLException {
        return reload(LazyQueryConstants.SELECT_TABLES_FOR_REFRESH, table);
    }

    @Override
    public void loadDetails(TreeNode table) throws SQLException {
        logger.info("Loading details for table \"" + table.getAttribute(NAME) + "\"...");
        this.addCategories(table);

        String tableName = table.getAttribute(NAME);
        table.getChildren().get(COLUMNS).setChildren(loadDetails(table, COLUMN, SELECT_TABLE_COLUMNS));
        table.getChildren().get(INDEXES).setChildren(this.loadIndexes(tableName));
        table.getChildren().get(CONSTRAINTS).setChildren(loadDetails(table, CONSTRAINT, SELECT_CONSTRAINTS));
        table.getChildren().get(TRIGGERS).setChildren(loadDetails(table, TRIGGER, SELECT_TRIGGERS));
        logger.info("Details for table \"" + table.getAttribute(NAME) + "\" loaded");

    }

    private void addCategories(TreeNode table) {
        table.setChildren(new ArrayList<>(Arrays.asList(
                new TreeNode(GrammarConstants.CategoryNames.COLUMNS),
                new TreeNode(GrammarConstants.CategoryNames.INDEXES),
                new TreeNode(GrammarConstants.CategoryNames.CONSTRAINTS),
                new TreeNode(GrammarConstants.CategoryNames.TRIGGERS)
        )));
    }

    private List<TreeNode> loadIndexes(String tableName) throws SQLException {

        ResultSet resultSet = this.loadStatement(LazyQueryConstants.SELECT_INDEXES, tableName);

        List<TreeNode> indexes = new ArrayList<>();

        while (resultSet.next()) {
            Map<String, String> attributes = new HashMap<>();
            String indexName = resultSet.getString(2);
            attributes.put(NAME, resultSet.getString(1));
            attributes.put(AttributeNameConstants.INDEX_NAME, indexName);
            TreeNode index = new TreeNode();
            index.setAttributes(attributes);
            if (indexName.equalsIgnoreCase(GrammarConstants.NodeNames.PRIMARY)) {
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
