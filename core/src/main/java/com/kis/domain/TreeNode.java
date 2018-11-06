package com.kis.domain;


import com.kis.serializer.Serializer;
import com.kis.utils.PropertyReader;
import com.kis.utils.PathConstant;
import org.jdom.IllegalNameException;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class TreeNode implements Serializable {

    private static final PropertyReader propertyReader = new PropertyReader(PathConstant.languagePath);

    private Map<String, String> attributes = new HashMap<>();

    private String type;

    private String text;

    private TreeNode parent;

    private List<TreeNode> children;

    static private Serializer serializer;

    public TreeNode() {
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
    }

    public TreeNode(String type) {
        this.setType(type);
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
    }

    public TreeNode(TreeNode treeNode) {
        this.type = treeNode.getType();
        this.text = treeNode.getText();
        this.attributes = new HashMap<>(treeNode.getAttributes());
        this.children = new ArrayList<>();
    }

    public TreeNode(String type, Map<String, String> attributes) {

        this.setType(type);
        this.setAttributes(attributes);
        this.children = new ArrayList<>();
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public void addChild(TreeNode treeNode) {
        TreeNode temp = this;
        while (temp.getParent() != null) {
            temp = temp.getParent();
            if (temp == treeNode) {
                try {
                    throw new Exception(propertyReader.getBundle().getString("LoopingOfTheNode"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (treeNode == this)
            throw new IllegalArgumentException(propertyReader.getBundle().getString("LoopingOfTheNode"));
        treeNode.parent = this;

        if (this.children == null)
            children = new ArrayList<>();
        this.children.add(treeNode);
    }

    /**
     * @param name       - param from console'GrammarConstants command '-tag'
     * @param attr       - param from console'GrammarConstants command '-attr'
     * @param value      - param from console'GrammarConstants command '-value'
     * @param text       - param from console'GrammarConstants command '-text'
     * @param searchMode - param from console'GrammarConstants command '-mode'
     * @return list of nodes, which are meet the requirements
     * @throws Exception
     */
    public List<TreeNode> search(String name, String attr, String value, String text, String searchMode, boolean accuracy) {
        List<TreeNode> result = new ArrayList<>();
        if (name == null && attr == null && value == null && text == null) {
            result.add(this);
            return result;
        }


        if (searchMode == null || searchMode.equals(propertyReader.getBundle().getString("BreadthMode")))
            this.breadthFirstSearch(result, name, attr, value, text, accuracy);
        else if (searchMode.equals(propertyReader.getBundle().getString("DepthMode")))
            this.depthFirstSearch(result, this, name, attr, value, text, accuracy);
        else {
            throw new IllegalArgumentException(propertyReader.getBundle().getString("UnknownSearchMode"));
        }

        return result;
    }

    /**
     * @param result - list, which will be a container for nodes
     *               It is the method for searching of nodes in depth
     */
    private void depthFirstSearch(List<TreeNode> result, TreeNode treeNode, String name, String attr, String value, String text, boolean accuracy) {
        for (TreeNode child : treeNode.getChildren()) {
            if (isConfirm(child, name, attr, value, text, accuracy)) {
                result.add(child);
            } else {
                depthFirstSearch(result, child, name, attr, value, text, accuracy);
            }
        }
    }

    /**
     * @param result - list, which will be a container for nodes
     *               It is the method for searching of nodes in breadth
     */
    private void breadthFirstSearch(List<TreeNode> result, String name, String attr, String value, String text, boolean accuracy) {
        LinkedList<TreeNode> list = new LinkedList<>();
        list.add(this);
        do {
            TreeNode child = list.getFirst();
            if (isConfirm(child, name, attr, value, text, accuracy)) {
                result.add(child);
            } else
                list.addAll(child.getChildren());
            list.removeFirst();
        } while (!list.isEmpty());
    }

    public boolean is(String name) {
        return this.type.equals(name);
    }

    /**
     * It'GrammarConstants method, which decide if the treeNode is meet the requirements
     *
     * @return true, if it'GrammarConstants meet the requirements
     */
    private boolean isConfirm(TreeNode treeNode, String name, String attr, String value, String text, boolean accuracy) {
        return ((name == null || treeNode.is(name))
                && (attr == null || treeNode.getAttribute(attr) != null)
                && (value == null || (attr != null && valueConfirm(treeNode.getAttribute(attr), value, accuracy))) || treeNode.getAttributes().containsValue(value))
                && (text == null || treeNode.getText().equals(text));
    }

    private boolean valueConfirm(String nodeValue, String value, boolean accuracy) {
        if (accuracy)
            return nodeValue.toLowerCase().equals(value.toLowerCase());
        return nodeValue.toLowerCase().contains(value.toLowerCase());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || type.equals(""))
            throw new NullPointerException(propertyReader.getBundle().getString("NullNodeName"));

        if (type.toCharArray()[0] >= 48 && type.toCharArray()[0] <= 57)
            throw new IllegalNameException(propertyReader.getBundle().getString("WrongNodeName"));
        this.type = type;
    }

    public void addAttribute(String key, String value) {
        if (key == null || value == null || value.equals(""))
            throw new NullPointerException();
        attributes.put(key, value);

    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        if (attributes != null) {
            this.attributes = attributes;
        } else
            this.attributes = new HashMap<>();

        if (attributes.get(null) != null) {
            throw new NullPointerException(propertyReader.getBundle().getString("NullKeyOfAttributes"));
        }
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode setParent(TreeNode parent) {

        if (parent == this) {
            throw new IllegalArgumentException(propertyReader.getBundle().getString("LoopingOfTheNode"));
        }
        if (this.parent != null)
            this.parent.children.remove(this);
        this.parent = parent;
        parent.addChild(this);
        return parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        for (TreeNode treeNode : children) {
            if (treeNode == this) {
                throw new IllegalArgumentException(propertyReader.getBundle().getString("LoopingOfTheNode"));
            }
            treeNode.setParent(this);
        }
        this.children = children;
    }

    /**
     * It'GrammarConstants method for comparing of nodes
     *
     * @return true, if the both tree are equal
     */
    public boolean equalsTree(TreeNode treeNode) {
        if (this == treeNode) return true;
        List<TreeNode> nodes_1 = new LinkedList<>();
        nodes_1.add(this);
        List<TreeNode> nodes_2 = new LinkedList<>();
        nodes_2.add(treeNode);
        do {
            if (!nodes_1.get(0).equals(nodes_2.get(0)))
                return false;
            nodes_1.addAll(nodes_1.get(0).getChildren());
            nodes_2.addAll(nodes_2.get(0).getChildren());


            nodes_1.remove(0);
            nodes_2.remove(0);
        } while (!nodes_1.isEmpty() && !nodes_2.isEmpty());

        return nodes_1.isEmpty() && nodes_2.isEmpty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode treeNode = (TreeNode) o;

        if (type != null ? !type.equals(treeNode.getType()) : treeNode.getType() != null) return false;
        if (text != null ? !text.equals(treeNode.getText()) : treeNode.text != null) return false;
        return attributes != null ? attributes.equals(treeNode.getAttributes()) : treeNode.attributes == null;

    }

    @Override
    public String toString() {
        if (attributes.get("NAME") == null)
            return type;
        return attributes.get("NAME");

    }

    public String serialize(File file) {
        return serializer.serialize(this, file);
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer newSerializer) {
        serializer = newSerializer;
    }
}