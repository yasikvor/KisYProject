package com.kis.task;

import com.kis.domain.TreeNode;
import com.kis.model.ConnectionModel;
import com.kis.service.TreeNodeService;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

public class LoadNodeTask extends Task<Void> {

    private TreeItem<TreeNode> selectedItem;

    public LoadNodeTask(TreeItem<TreeNode> selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    protected Void call() throws Exception {

        while (ConnectionModel.getConnection() == null)
            Thread.sleep(100);

        selectedItem.setExpanded(true);
        TreeNodeService.downloadNode(selectedItem.getValue());
        TreeNodeService.updateTreeNode(selectedItem);

        return null;
    }
}
