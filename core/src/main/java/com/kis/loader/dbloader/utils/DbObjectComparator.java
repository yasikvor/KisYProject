package com.kis.loader.dbloader.utils;

import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;

public class DbObjectComparator {
    public static boolean isDbObject(TreeNode treeNode) {
        return treeNode.is(GrammarConstants.NodeNames.TABLE) ||
                treeNode.is(GrammarConstants.NodeNames.VIEW) ||
                treeNode.is(GrammarConstants.NodeNames.PROCEDURE) ||
                treeNode.is(GrammarConstants.NodeNames.FUNCTION);
    }

    public static boolean isCategory(TreeNode treeNode) {
        return treeNode.is(GrammarConstants.CategoryNames.TABLES) ||
                treeNode.is(GrammarConstants.CategoryNames.VIEWS) ||
                treeNode.is(GrammarConstants.CategoryNames.PROCEDURES) ||
                treeNode.is(GrammarConstants.CategoryNames.FUNCTIONS);
    }

    public static boolean hasSql(TreeNode treeNode) {
        return isDbObject(treeNode)
                || treeNode.is(GrammarConstants.NodeNames.TRIGGER);
    }
}
