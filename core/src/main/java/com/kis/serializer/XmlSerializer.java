package com.kis.serializer;

import com.kis.domain.TreeNode;

import com.kis.utils.PropertyReader;
import com.kis.utils.PathConstant;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


import java.io.*;
import java.util.*;

class XmlSerializer implements Serializer {

    private static Logger logger = Logger.getLogger(XmlSerializer.class.getName());

    private PropertyReader propertyReader;

    static final Serializer INSTANCE = new XmlSerializer();

    private XmlSerializer() {
        propertyReader = new PropertyReader(PathConstant.languagePath);
    }
    /**
     *
     * @param treeNode - treeNode, which we have to serialize
     * @param file - output file. If null - XML-tree will be written on console
     * @return text of the XML-file
     *
     */
    @Override
    public String serialize(TreeNode treeNode, File file) {

        Element root = convertToElement(treeNode);
        return output(root, file);
    }

    private Element convertToElement(TreeNode treeNode) {
        if(treeNode.getType() == null || treeNode.is(""))
            throw new IllegalArgumentException(propertyReader.getBundle().getString("NullNodeName"));
        Element root = new Element(treeNode.getType());
        for(Map.Entry<String, String> entry : treeNode.getAttributes().entrySet()) {
            root.setAttribute(entry.getKey(), entry.getValue());
        }
        root.setText(treeNode.getText());
        writeTree(treeNode.getChildren(), root);
        return root;
    }

    /**
     *
     * @param root - root element for the tree
     * @param file - output file. If null - text will be written on console
     * @return text of the XML-file
     */
    private String output(Element root, File file) {
        Document document = new Document(root);
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());

        if(file != null) {
            try {
                if(!file.getName().endsWith(".xml"))
                    throw new IllegalArgumentException(propertyReader.getBundle().getString("IncorrectFileName"));
                out.output(document, new FileOutputStream(file.getAbsolutePath()));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            logger.info("Save xml in file " + file.getName());
            return out.outputString(document);
        }
        return null;
    }

    /**
     * It'GrammarConstants method for writing tree into file
     * @param children - node.getChildren()
     * @param parent - parent element in which will be recorded it'GrammarConstants children
     */
    private void writeTree(List<TreeNode> children, Element parent) {

        List<Element> elements = new ArrayList<>();
        for (TreeNode child : children){
            Element element = new Element(child.getType());
            List<Attribute> attributes = new ArrayList<>();
            for(Map.Entry<String, String> entry : child.getAttributes().entrySet()) {
                attributes.add(new Attribute(entry.getKey(), entry.getValue()));
            }

            String text = child.getText();
            if(text != null) {
                CDATA cdata = new CDATA(text);
                element.setContent(cdata);
            }

            element.setAttributes(attributes);
            elements.add(element);
            writeTree(child.getChildren(), element);

        }
        parent.addContent(elements);
    }




}