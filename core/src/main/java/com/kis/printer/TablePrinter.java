package com.kis.printer;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.printer.annotation.PrinterInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@PrinterInfo(type = GrammarConstants.NodeNames.TABLE)
class TablePrinter implements Printer {

    /**
     * @param table - node, which will be printed
     * @return string of a printed node
     */
    @Override
    public String print(TreeNode table) {
        StringBuilder mainScript = new StringBuilder();
        String tableName = table.getAttribute(AttributeNameConstants.NAME);
        mainScript.append("DROP TABLE IF EXISTS `").append(tableName).append("`;").append(System.lineSeparator());
        mainScript.append("CREATE TABLE `").append(tableName).append("` (").append(System.lineSeparator());

        List<TreeNode> details = table.getChildren();
        List<TreeNode> items = details.get(0).getChildren();
        mainScript.append(this.getColumns(items));

        items = details.get(1).getChildren();

        mainScript.append(this.getIndexes(items, GrammarConstants.NodeNames.PRIMARY));
        mainScript.append(this.getIndexes(items, GrammarConstants.NodeNames.UNIQUE));
        mainScript.append(this.getIndexes(items, GrammarConstants.NodeNames.KEY));

        items = details.get(2).getChildren();
        mainScript.append(this.getConstraints(items));
        mainScript.deleteCharAt(mainScript.length() - 2);

        mainScript.append(System.lineSeparator());
        mainScript.append(") ENGINE=").append(table.getAttribute(AttributeNameConstants.ENGINE));
        mainScript.append(" DEFAULT CHARSET=")
                .append(table.getAttribute(AttributeNameConstants.TABLE_COLLATION))
                .append(";")
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        return mainScript.toString();
    }


    private String getColumns(List<TreeNode> columns) {
        StringBuilder script = new StringBuilder();
        for (TreeNode column : columns) {
            Map<String, String> attributes = column.getAttributes();
            script.append("\t`").append(attributes.get(AttributeNameConstants.NAME)).append("` ").append(attributes.get(AttributeNameConstants.COLUMN_TYPE)).append(" ");
            if (attributes.get(AttributeNameConstants.IS_NULLABLE).equals("NO")) {
                script.append("NOT NULL");
            }
            if (attributes.get(AttributeNameConstants.COLUMN_DEFAULT) != null) {
                script.append("DEFAULT `").append(attributes.get(AttributeNameConstants.COLUMN_DEFAULT)).append("`");
            } else if (attributes.get("IS_NULLABLE").equals("YES") && attributes.get(AttributeNameConstants.COLUMN_DEFAULT) == null)
                script.append("DEFAULT NULL");

            if (attributes.get(AttributeNameConstants.EXTRA) != null) {
                script.append(" ").append(attributes.get(AttributeNameConstants.EXTRA).toUpperCase());
            }
            script.append(",").append(System.lineSeparator());
        }

        return script.toString();
    }

    private String getIndexes(List<TreeNode> allIndexes, String indexType) {

        if (indexType.equals(GrammarConstants.NodeNames.PRIMARY)) {
            return this.getPrimaryKeys(this.findIndexes(allIndexes, indexType));
        } else if (indexType.contains(GrammarConstants.NodeNames.UNIQUE)) {
            return this.getUniqueKeys(this.findIndexes(allIndexes, indexType));
        } else {
            return this.getKeys(this.findIndexes(allIndexes, indexType));
        }
    }

    private String getPrimaryKeys(List<TreeNode> indexes) {
        if (indexes.isEmpty()) return "";

        StringBuilder script = new StringBuilder();
        script.append("\tPRIMARY KEY (");
        indexes.forEach(index -> script.
                append("`").
                append(index.getAttribute(AttributeNameConstants.NAME))
                .append("`,"));
        script.deleteCharAt(script.length() - 1);
        script.append("),").append(System.lineSeparator());
        return script.toString();
    }

    private String getUniqueKeys(List<TreeNode> indexes) {
        if (indexes.isEmpty()) return "";

        StringBuilder script = new StringBuilder();
        for (TreeNode index : indexes) {
            script.append("\tUNIQUE KEY `")
                    .append(index.getAttribute(AttributeNameConstants.INDEX_NAME))
                    .append("` (`").append(index.getAttribute(AttributeNameConstants.NAME))
                    .append("`),").append(System.lineSeparator());
        }
        return script.toString();
    }

    private String getKeys(List<TreeNode> indexes) {
        if (indexes.isEmpty()) return "";

        StringBuilder script = new StringBuilder();
        indexes.forEach(index ->
                script.append("\tKEY `")
                .append(index.getAttribute(AttributeNameConstants.INDEX_NAME))
                .append("` (`").append(index.getAttribute(AttributeNameConstants.NAME)).append("`),")
                .append(System.lineSeparator()));
        return script.toString();
    }

    private String getConstraints(List<TreeNode> constraints) {
        if (constraints.isEmpty()) return "";

        StringBuilder script = new StringBuilder();
        for (TreeNode constraint : constraints) {
            Map<String, String> attributes = constraint.getAttributes();
            script.append("\tCONSTRAINT `")
                    .append(attributes.get(AttributeNameConstants.NAME))
                    .append("` FOREIGN KEY (`").append(attributes.get(AttributeNameConstants.COLUMN_NAME)).append("`) ")
                    .append("REFERENCES `").append(attributes.get(AttributeNameConstants.REFERENCED_TABLE_NAME)).append("` (`")
                    .append(attributes.get(AttributeNameConstants.REFERENCED_COLUMN_NAME)).append("`)," + System.lineSeparator());
        }

        return script.toString();
    }

    private List<TreeNode> findIndexes(List<TreeNode> allIndexes, String indexType) {
        return allIndexes.stream().
                filter(index -> index.is(indexType))
                .collect(Collectors.toList());
    }
}
