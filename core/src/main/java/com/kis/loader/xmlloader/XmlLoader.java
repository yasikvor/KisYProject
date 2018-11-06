package com.kis.loader.xmlloader;

import com.kis.domain.TreeNode;

import com.kis.loader.dbloader.LoaderManager;
import com.kis.utils.PathConstant;
import com.kis.utils.PropertyReader;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.util.*;

public class XmlLoader {

    private PropertyReader propertyReader = new PropertyReader(PathConstant.languagePath);

    /**
     * @param inputStream - it'GrammarConstants file, which the user want to deserialize
     * @return TreeNode node from this file
     */
    public TreeNode load(InputStream inputStream) {

        SAXBuilder builder = new SAXBuilder();
        Document document = null;


        try {
            document = builder.build(inputStream);
            inputStream.close();
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }

        return getRoot(document);
    }

    public TreeNode load(File file) {
        if (file.length() == 0)
            throw new IllegalArgumentException(propertyReader.getBundle().getString("NullFile"));

        if (!file.getName().endsWith(".xml"))
            throw new IllegalArgumentException(propertyReader.getBundle().getString("IncorrectFileName"));

        try {
            return load(new FileInputStream(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * It'GrammarConstants method for reading XML-file
     *
     * @param children - elements, which will be recorded in parent
     * @param parent   - node
     */
    private void readTree(List<Element> children, TreeNode parent) {
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        do {

            if (children.isEmpty())
                continue;
            Element child = children.get(0);
            Map<String, String> attributes = new HashMap<>();
            List<Attribute> attributeArrayList = new ArrayList<>(child.getAttributes());
            attributeArrayList.forEach(attribute -> attributes.put(attribute.getName(), attribute.getValue()));

            TreeNode treeNode = new TreeNode(child.getName(), attributes);
            char[] chars = child.getText().toCharArray();

            for (char aChar : chars) {
                if (aChar != '\n' && aChar != ' ') {

                    treeNode.setText(child.getText());
                    break;
                }
            }

            treeNodes.add(treeNode);
            readTree(child.getChildren(), treeNode);
            children.remove(0);
        } while (!children.isEmpty());
        parent.setChildren(treeNodes);

    }

    private TreeNode getRoot(Document document) {
        if (document == null) {
            return null;
        }
        Element rootElement = document.getRootElement();

        List<Attribute> attrList = new ArrayList<>(rootElement.getAttributes());
        Map<String, String> treeNodeAttributes = new HashMap<>();

        attrList.forEach(attribute -> treeNodeAttributes.put(attribute.getName(), attribute.getValue()));
        TreeNode root = new TreeNode(rootElement.getName(), treeNodeAttributes);

        readTree(rootElement.getChildren(), root);

        return root;
    }
}
