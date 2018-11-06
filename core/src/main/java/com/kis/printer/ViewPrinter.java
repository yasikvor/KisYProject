package com.kis.printer;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.printer.annotation.PrinterInfo;

import java.util.Map;

@PrinterInfo(type = GrammarConstants.NodeNames.VIEW)
class ViewPrinter implements Printer {

    /**
     *
     * @param view - node, which will be printed
     * @return string of a printed node
     */
    @Override
    public String print(TreeNode view) {

        StringBuilder mainScript = new StringBuilder();

            mainScript.append("DROP TABLE IF EXISTS `").append(view.getAttribute(AttributeNameConstants.NAME)).append("`;").append(System.lineSeparator());
            Map<String, String> attributes = view.getAttributes();
            mainScript.append("CREATE VIEW `").append(attributes.get(AttributeNameConstants.NAME))
                    .append("` AS ").append(attributes.get(AttributeNameConstants.VIEW_DEFINITION)).append(";").append(System.lineSeparator()).append(System.lineSeparator());
        return mainScript.toString();
    }
}
