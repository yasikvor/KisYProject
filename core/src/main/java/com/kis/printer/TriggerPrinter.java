package com.kis.printer;

import com.kis.constant.AttributeNameConstants;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.printer.annotation.PrinterInfo;

import java.util.Map;

@PrinterInfo(type = GrammarConstants.NodeNames.TRIGGER)
class TriggerPrinter implements Printer {

    @Override
    public String print(TreeNode trigger) {
        TreeNode table = trigger.getParent().getParent();

        StringBuilder script = new StringBuilder();
        Map<String, String> attributes = trigger.getAttributes();
        script.append("CREATE TRIGGER `").append(attributes.get(AttributeNameConstants.NAME)).append("` ")
                .append(attributes.get(AttributeNameConstants.ACTION_TIMING))
                .append(" ").append(attributes.get(AttributeNameConstants.EVENT_MANIPULATION))
                .append(" ON `").append(table.getAttribute(AttributeNameConstants.NAME)).append("` ")
                .append("FOR EACH ").append(attributes.get(AttributeNameConstants.ACTION_ORIENTATION))
                .append(" ").append(attributes.get(AttributeNameConstants.ACTION_STATEMENT)).append(";")
                .append(System.lineSeparator()).append(System.lineSeparator());

        return script.toString();
    }
}
