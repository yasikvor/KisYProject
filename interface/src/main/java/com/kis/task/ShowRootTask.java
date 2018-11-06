package com.kis.task;

import com.kis.model.NodeModel;
import com.kis.service.TreeNodeService;
import com.kis.viewmodel.ViewModelRepository;
import javafx.concurrent.Task;

public class ShowRootTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        while (NodeModel.getRoot() == null)
            Thread.sleep(100);
        TreeNodeService.createRoot();
        ViewModelRepository.getTreeNodeViewModel().createRoot();
        return null;
    }
}
