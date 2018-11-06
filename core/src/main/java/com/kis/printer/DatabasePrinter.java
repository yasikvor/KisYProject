package com.kis.printer;

import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.printer.annotation.PrinterInfo;

import java.util.Collection;

@PrinterInfo(type = GrammarConstants.NodeNames.SCHEMA)
public class DatabasePrinter implements Printer{

    @Override
    public String print(TreeNode treeNode) {
        StringBuilder mainScript = new StringBuilder();
        treeNode.getChildren().stream()
                .map(TreeNode::getChildren)
                .flatMap(Collection::stream)
                .forEach(object -> mainScript.append(PrinterManager.print(object)));

        return mainScript.toString();
    }
}
