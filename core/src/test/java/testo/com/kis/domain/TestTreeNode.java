package testo.com.kis.domain;

import com.kis.domain.TreeNode;
import org.jdom.IllegalNameException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;


public class TestTreeNode {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void caseSendItselfToItsChildrenByAddChildren() {
        thrown.expect(IllegalArgumentException.class);
        TreeNode treeNode = new TreeNode("Name");
        List<TreeNode> list = new ArrayList<>();
        list.add(treeNode);
        treeNode.setChildren(list);
    }

    @Test
    public void caseSendItselfToItsChildrenByAddChild() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        TreeNode treeNode = new TreeNode("Name");
        treeNode.addChild(treeNode);

    }

    @Test
    public void caseSendItselfToItsParent() {
        thrown.expect(IllegalArgumentException.class);
        TreeNode treeNode = new TreeNode("Name");
        treeNode.setParent(treeNode);
    }

    @Test
    public void caseSendNormalValues() {
        Map<String, String> map = new HashMap<>();
        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        TreeNode parent = new TreeNode("name");
        List<TreeNode> children = new LinkedList<>();
        TreeNode treeNode = new TreeNode("NormalName", map);
        treeNode.setParent(parent);
        treeNode.setChildren(children);

        Assert.assertEquals(treeNode.getType(), "NormalName");
        Assert.assertEquals(treeNode.getAttributes(), map);
        Assert.assertEquals(treeNode.getParent(), parent);
        Assert.assertEquals(treeNode.getChildren(), children);

    }

    @Test
    public void caseSendNullKeyToAttribute() {
        thrown.expect(NullPointerException.class);
        Map<String, String> map = new HashMap<>();
        map.put(null, "value");
        TreeNode treeNode = new TreeNode("name", map);
    }

    @Test
    public void caseSendNumberAtTheBeginningOfName() {

        thrown.expect(IllegalNameException.class);
        TreeNode treeNode = new TreeNode("1234");
    }

    @Test
    public void caseSendNullNameInTreeNode() {

        thrown.expect(NullPointerException.class);
        TreeNode treeNode = new TreeNode();
        treeNode.setType(null);
    }
}
