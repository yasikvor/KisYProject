package com.kis.printer;

import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.printer.annotation.PrinterInfo;
import com.kis.constant.AttributeNameConstants;

import java.util.ArrayList;
import java.util.Map;


@PrinterInfo(type = GrammarConstants.NodeNames.PROCEDURE)
class ProcedurePrinter implements Printer{
    /**
     *
     * @param procedure - node, which will be printed
     * @return string of a printed node
     */
    @Override
    public String print(TreeNode procedure) {
        StringBuilder mainScript = new StringBuilder();

        Map<String, String> attributes = procedure.getAttributes();
        mainScript.append("CREATE DEFINER = `")
                .append(attributes.get(AttributeNameConstants.DEFINER))
                .append("` ")
                .append(GrammarConstants.NodeNames.PROCEDURE.toUpperCase())
                .append(" `")
                .append(attributes.get(AttributeNameConstants.NAME))
                .append("` (");
        ArrayList<TreeNode> parameters = new ArrayList<>(procedure.getChildren());

        for (TreeNode parameter : parameters) {
            Map<String, String> attr = parameter.getAttributes();
            mainScript.append(attr.get(AttributeNameConstants.PARAMETER_MODE))
                    .append(" ").append(attr.get(AttributeNameConstants.NAME))
                    .append(" ").append(attr.get(AttributeNameConstants.DTD_IDENTIFIER)).append(", ");
        }
        if(parameters.size() != 0)
            mainScript.deleteCharAt(mainScript.length() - 2);
        mainScript.append(")").append(System.lineSeparator());
        mainScript.append(attributes.get(AttributeNameConstants.ROUTINE_DEFINITION))
                .append(";")
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        return mainScript.toString();
    }
}
