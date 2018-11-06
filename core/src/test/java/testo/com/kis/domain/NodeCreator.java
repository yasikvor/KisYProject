package testo.com.kis.domain;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.LoaderManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class NodeCreator {

    private static NodeCreator instance;

    private TreeNode treeNode = null;

    private NodeCreator() {

    }

    public static NodeCreator getInstance() {
        if (instance == null)
            instance = new NodeCreator();
        return instance;
    }

    public void getDetails(TreeNode database) throws SQLException {
        database = LoaderManager.getRoot();
        for (TreeNode category : database.getChildren()) {
            for(TreeNode object : category.getChildren()) {
                LoaderManager.loadDetails(object);
            }
        }
    }

    public TreeNode getTreeNode() {
        return testNode();
    }

    private TreeNode testNode(){

        Map<String, String> databaseAttr = new HashMap<>();
        databaseAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.SPECIFIC_SCHEMA);
        TreeNode database = new TreeNode(GrammarConstants.NodeNames.SCHEMA, databaseAttr);
        {
            TreeNode tables = new TreeNode(GrammarConstants.CategoryNames.TABLES);
            database.addChild(tables);
            {
                Map<String, String> tableAttr = new HashMap<>();
                tableAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.TABLE_NAME);
                tableAttr.put(AttributeNameConstants.ENGINE, AttributeNameConstants.ENGINE);
                tableAttr.put(AttributeNameConstants.TABLE_COLLATION, AttributeNameConstants.TABLE_COLLATION);
                TreeNode table = new TreeNode(GrammarConstants.NodeNames.TABLE, tableAttr);
                tables.addChild(table);
                {
                    TreeNode columns = new TreeNode(GrammarConstants.CategoryNames.COLUMNS);
                    table.addChild(columns);
                    {
                        Map<String, String> columnAttr = new HashMap<>();
                        TreeNode column = new TreeNode(GrammarConstants.NodeNames.COLUMN, columnAttr);
                        columnAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.COLUMN_NAME);
                        columnAttr.put(AttributeNameConstants.IS_NULLABLE, AttributeNameConstants.IS_NULLABLE);
                        columnAttr.put(AttributeNameConstants.EXTRA, AttributeNameConstants.EXTRA);

                        columnAttr.put(AttributeNameConstants.COLUMN_TYPE, AttributeNameConstants.COLUMN_TYPE);
                        columnAttr.put(AttributeNameConstants.COLUMN_DEFAULT, AttributeNameConstants.COLUMN_DEFAULT);
                        columns.addChild(column);
                    }
                    TreeNode indexes = new TreeNode(GrammarConstants.CategoryNames.INDEXES);
                    table.addChild(indexes);
                    {
                        Map<String, String> index1Attr = new HashMap<>();
                        index1Attr.put(AttributeNameConstants.NAME, GrammarConstants.NodeNames.PRIMARY.toUpperCase());
                        index1Attr.put(AttributeNameConstants.INDEX_NAME, GrammarConstants.NodeNames.PRIMARY.toUpperCase());
                        TreeNode index1 = new TreeNode(GrammarConstants.NodeNames.PRIMARY, index1Attr);
                        indexes.addChild(index1);

                        Map<String, String> index2Attr = new HashMap<>();
                        index2Attr.put(AttributeNameConstants.NAME, GrammarConstants.NodeNames.PRIMARY.toUpperCase());
                        index2Attr.put(AttributeNameConstants.INDEX_NAME, GrammarConstants.NodeNames.PRIMARY.toUpperCase());
                        TreeNode index2 = new TreeNode(GrammarConstants.NodeNames.PRIMARY, index2Attr);
                        indexes.addChild(index2);

                        Map<String, String> index3Attr = new HashMap<>();
                        index3Attr.put(AttributeNameConstants.NAME, "uniq_UNIQUE");
                        index3Attr.put(AttributeNameConstants.INDEX_NAME, "uniq_UNIQUE");
                        TreeNode index3 = new TreeNode(GrammarConstants.NodeNames.UNIQUE, index3Attr);
                        indexes.addChild(index3);

                        Map<String, String> index4Attr = new HashMap<>();
                        index4Attr.put(AttributeNameConstants.NAME, "Something");
                        index4Attr.put(AttributeNameConstants.INDEX_NAME, "Someone");
                        TreeNode index4 = new TreeNode(GrammarConstants.NodeNames.KEY, index4Attr);
                        indexes.addChild(index4);
                    }
                    TreeNode constraints = new TreeNode(GrammarConstants.CategoryNames.CONSTRAINTS);
                    table.addChild(constraints);
                    {
                        Map<String, String> constraintAttr = new HashMap<>();
                        constraintAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.CONSTRAINT_NAME);
                        constraintAttr.put(AttributeNameConstants.REFERENCED_COLUMN_NAME, AttributeNameConstants.REFERENCED_COLUMN_NAME);
                        constraintAttr.put(AttributeNameConstants.REFERENCED_TABLE_NAME, AttributeNameConstants.REFERENCED_TABLE_NAME);

                        constraintAttr.put(AttributeNameConstants.COLUMN_NAME, AttributeNameConstants.COLUMN_NAME);
                        TreeNode constraint = new TreeNode(GrammarConstants.NodeNames.CONSTRAINT, constraintAttr);
                        constraints.addChild(constraint);
                    }
                    TreeNode triggers = new TreeNode(GrammarConstants.CategoryNames.TRIGGERS);
                    table.addChild(triggers);
                    {
                        Map<String, String> triggerAttr = new HashMap<>();
                        triggerAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.TRIGGER_NAME);
                        triggerAttr.put(AttributeNameConstants.EVENT_MANIPULATION, AttributeNameConstants.EVENT_MANIPULATION);

                        triggerAttr.put(AttributeNameConstants.ACTION_ORIENTATION, AttributeNameConstants.ACTION_ORIENTATION);
                        triggerAttr.put(AttributeNameConstants.ACTION_TIMING, AttributeNameConstants.ACTION_TIMING);
                        triggerAttr.put(AttributeNameConstants.ACTION_STATEMENT, AttributeNameConstants.ACTION_STATEMENT);
                        TreeNode trigger = new TreeNode(GrammarConstants.NodeNames.TRIGGER, triggerAttr);
                        triggers.addChild(trigger);
                    }
                }
            }
            TreeNode views = new TreeNode(GrammarConstants.CategoryNames.VIEWS);
            database.addChild(views);
            {
                Map<String, String> viewAttr = new HashMap<>();
                viewAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.TABLE_NAME);
                viewAttr.put(AttributeNameConstants.DEFINER, AttributeNameConstants.DEFINER);
                viewAttr.put(AttributeNameConstants.VIEW_DEFINITION, AttributeNameConstants.VIEW_DEFINITION);
                TreeNode view = new TreeNode(GrammarConstants.NodeNames.VIEW, viewAttr);
                views.addChild(view);
                {

                        Map<String, String> columnAttr = new HashMap<>();
                        columnAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.COLUMN_NAME);
                        columnAttr.put(AttributeNameConstants.COLUMN_TYPE, AttributeNameConstants.COLUMN_TYPE);
                        TreeNode column = new TreeNode(GrammarConstants.NodeNames.COLUMN, columnAttr);
                        view.addChild(column);
                    }

            }
            TreeNode procedures = new TreeNode(GrammarConstants.CategoryNames.PROCEDURES);
            database.addChild(procedures);
            {

                Map<String, String> procedureAttr = new HashMap<>();
                procedureAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.ROUTINE_NAME);
                procedureAttr.put(AttributeNameConstants.DEFINER, AttributeNameConstants.DEFINER);
                procedureAttr.put(AttributeNameConstants.ROUTINE_TYPE, GrammarConstants.NodeNames.PROCEDURE.toUpperCase());
                procedureAttr.put(AttributeNameConstants.ROUTINE_DEFINITION, AttributeNameConstants.ROUTINE_DEFINITION);
                TreeNode procedure = new TreeNode(GrammarConstants.NodeNames.PROCEDURE, procedureAttr);
                procedures.addChild(procedure);
                {
                    Map<String, String> parameterAttr = new HashMap<>();
                    parameterAttr.put(AttributeNameConstants.PARAMETER_MODE, AttributeNameConstants.PARAMETER_MODE);
                    parameterAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.PARAMETER_NAME);
                    parameterAttr.put(AttributeNameConstants.DTD_IDENTIFIER, AttributeNameConstants.DTD_IDENTIFIER);
                    TreeNode parameter = new TreeNode(GrammarConstants.NodeNames.PARAMETER, parameterAttr);
                    procedure.addChild(parameter);
                }
            }
            TreeNode functions = new TreeNode(GrammarConstants.CategoryNames.FUNCTIONS);
            database.addChild(functions);
            {
                Map<String, String> procedureAttr = new HashMap<>();
                procedureAttr.put(AttributeNameConstants.DEFINER, AttributeNameConstants.DEFINER);
                procedureAttr.put(AttributeNameConstants.ROUTINE_TYPE, GrammarConstants.NodeNames.FUNCTION.toUpperCase());
                procedureAttr.put(AttributeNameConstants.NAME, AttributeNameConstants.ROUTINE_NAME);
                procedureAttr.put(AttributeNameConstants.ROUTINE_DEFINITION, AttributeNameConstants.ROUTINE_DEFINITION);
                TreeNode function = new TreeNode(GrammarConstants.NodeNames.FUNCTION, procedureAttr);
                functions.addChild(function);
                {
                    Map<String, String> parameterAttr = new HashMap<>();
                    parameterAttr.put(AttributeNameConstants.DTD_IDENTIFIER, AttributeNameConstants.DTD_IDENTIFIER);
                    parameterAttr.put(AttributeNameConstants.SPECIFIC_SCHEMA, AttributeNameConstants.SPECIFIC_SCHEMA);
                    TreeNode parameter = new TreeNode(GrammarConstants.NodeNames.RETURN, parameterAttr);
                    function.addChild(parameter);
                }
            }
        }
        return database;
    }
}
