package com.kis.service;

import com.kis.connection.ConnectionRepository;
import com.kis.constant.ConnectionObjectConstant;
import com.kis.domain.TreeNode;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeStringConverter extends StringConverter<TreeNode> {

    private String nodeType;

    private String nodeName = null;

    private String parentType;

    public NodeStringConverter(String nodeType, String parentType) {
        this.parentType = parentType;
        this.nodeType = nodeType;
    }

    @Override
    public String toString(TreeNode object) {
        if(object == null)
            return "";
        return object.toString();
    }

    @Override
    public TreeNode fromString(String string) {
        if(string == null)
            return null;
        nodeName = string;
        TreeNode parentNode = ConnectionRepository.getNode(parentType);
        if(parentNode != null) {
            List<TreeNode> nodes = ConnectionRepository.getNode(parentType).search(nodeType, ConnectionObjectConstant.NAME, string, null, null, true);
            if(nodes.size() == 1) {
                ConnectionRepository.rememberNode(nodes.get(0));
                return nodes.get(0);
            }
        }


        Map<String, String> attributes = new HashMap<>();
        attributes.put(ConnectionObjectConstant.NAME, string);
        TreeNode node = new TreeNode(nodeType, attributes);
        ConnectionRepository.rememberNode(node);
        return node;






    }
}
