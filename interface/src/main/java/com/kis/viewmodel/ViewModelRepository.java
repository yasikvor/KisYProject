package com.kis.viewmodel;

public class ViewModelRepository {

    private static ConnectionViewModel connectionViewModel;

    private static TreeNodeViewModel treeNodeViewModel;

    private static RootViewModel rootViewModel;

    public static ConnectionViewModel getConnectionViewModel() {
        return connectionViewModel;
    }

    public static void setConnectionViewModel(ConnectionViewModel connectionViewModel) {
        ViewModelRepository.connectionViewModel = connectionViewModel;
    }

    public static TreeNodeViewModel getTreeNodeViewModel() {
        return treeNodeViewModel;
    }

    public static void setTreeNodeViewModel(TreeNodeViewModel treeNodeViewModel) {
        ViewModelRepository.treeNodeViewModel = treeNodeViewModel;
    }

    public static RootViewModel getRootViewModel() {
        return rootViewModel;
    }

    public static void setRootViewModel(RootViewModel rootViewModel) {
        ViewModelRepository.rootViewModel = rootViewModel;
    }
}
