package com.kis.printer;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.printer.annotation.PrinterInfo;

import java.util.ArrayList;
import java.util.Map;

@PrinterInfo(type = GrammarConstants.NodeNames.FUNCTION)
class FunctionPrinter implements Printer {

    /**
     * @param function - node, which will be printed
     * @return string of a printed node
     */
    @Override
    public String print(TreeNode function) {
        StringBuilder mainScript = new StringBuilder();

        Map<String, String> attributes = function.getAttributes();
        mainScript.append("CREATE DEFINER = `")
                .append(attributes.get(AttributeNameConstants.DEFINER))
                .append("` ")
                .append(GrammarConstants.NodeNames.FUNCTION.toUpperCase())
                .append(" `")
                .append(attributes.get(AttributeNameConstants.NAME))
                .append("` (");
        ArrayList<TreeNode> children = new ArrayList<>(function.getChildren());
        for (TreeNode child : children) {
            Map<String, String> attr = child.getAttributes();
            mainScript.append(attr.get(AttributeNameConstants.NAME))
                    .append(" ")
                    .append(attr.get(AttributeNameConstants.DTD_IDENTIFIER))
                    .append(", ");
        }
        if (children.size() != 0)
            mainScript.deleteCharAt(mainScript.length() - 2);
        mainScript.append(") RETURNS ").append(function.getAttribute(AttributeNameConstants.DTD_IDENTIFIER)).append(System.lineSeparator());
        mainScript.append(attributes.get(AttributeNameConstants.ROUTINE_DEFINITION)).append(";").append(System.lineSeparator()).append(System.lineSeparator());
        return mainScript.toString();
    }

}
