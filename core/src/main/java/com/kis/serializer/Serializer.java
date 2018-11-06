package com.kis.serializer;

import com.kis.domain.TreeNode;

import java.io.File;

public interface Serializer {

    String serialize(TreeNode treeNode, File file);
}