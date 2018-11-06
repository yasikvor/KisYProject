package com.kis.service;

import com.kis.domain.TreeNode;
import com.kis.serializer.SerializerFactory;
import javafx.scene.control.TreeItem;

import java.io.File;

public class ScriptGenerator {

    public static void printScript(TreeItem<TreeNode> item, File file) {
        TreeNode node = item.getValue();
        SerializerFactory.getSqlSerializer().serialize(node, file);
    }
}
