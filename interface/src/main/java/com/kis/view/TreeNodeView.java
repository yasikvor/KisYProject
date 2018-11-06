package com.kis.view;

import com.kis.constant.SettingsConstant;
import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import com.kis.repository.LayoutRepository;
import com.kis.settings.Settings;
import com.kis.viewmodel.TreeNodeViewModel;
import com.kis.viewmodel.ViewModelRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class TreeNodeView {

    @FXML
    private TreeView<TreeNode> treeView;

    @FXML
    private TableView<Map.Entry<String, String>> tableView;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> attributeColumn;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> valueColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private MenuItem createSql;

    @FXML
    private MenuItem reload;

    @FXML
    private MenuItem loadFullDatabase;

    @FXML
    private ProgressIndicator loadIndicator;

    @FXML
    private Button returnTree;

    @FXML
    private TitledPane scriptTitledPane;

    @FXML
    private TitledPane propertyTitledPane;

    private TreeNodeViewModel viewModel;

    public TreeNodeView() {
        this.viewModel = new TreeNodeViewModel();
        ViewModelRepository.setTreeNodeViewModel(viewModel);
    }

    @FXML
    private void initialize() {

        loadFullDatabase.setOnAction(event -> {
            viewModel.loadFullDatabase();
            viewModel.textAreaProperty().set("");
            viewModel.tableVisibleProperty().set(false);
        });

        scriptTitledPane.contentProperty().bindBidirectional(viewModel.scriptProperty());
        scriptTitledPane.setExpanded(true);
        tableView.setVisible(false);
        createSql.setOnAction(event -> saveSql());
        reload.setOnAction(event -> reload());

        createSql.disableProperty().bindBidirectional(ViewModelRepository.getRootViewModel().createSqlDisabledProperty());
        reload.disableProperty().bindBidirectional(ViewModelRepository.getRootViewModel().reloadDisabledProperty());
        loadFullDatabase.disableProperty().bindBidirectional(ViewModelRepository.getRootViewModel().loadAllDatabaseDisabledProperty());
        returnTree.setOnAction(event -> viewModel.resetFilter());
        treeView.disableProperty().bindBidirectional(viewModel.treeViewIsDisabledProperty());

        loadIndicator.visibleProperty().bindBidirectional(viewModel.visibleIndicatorProperty());
        searchTextField.textProperty().bindBidirectional(viewModel.searchTextFieldProperty());
        tableView.itemsProperty().bindBidirectional(viewModel.tableViewProperty());
        tableView.visibleProperty().bindBidirectional(viewModel.tableVisibleProperty());

        attributeColumn.setCellValueFactory((val) -> new SimpleStringProperty(val.getValue().getKey()));
        valueColumn.setCellValueFactory((val) -> new SimpleStringProperty(val.getValue().getValue()));


        treeView.rootProperty().bindBidirectional(viewModel.rootProperty());

        propertyTitledPane.setCollapsible(false);
        scriptTitledPane.setCollapsible(false);
        treeView.selectionModelProperty().get().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                viewModel.changeSelection(newValue);
            } catch (SQLException e) {
                viewModel.selectedItemProperty().set(newValue);
                newValue.setExpanded(true);
            }
        });


    }

    @FXML
    private void search() {
        viewModel.search();
    }

    @FXML
    private void saveSql() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQl files (*.sql)", "*.sql");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setTitle("Save script");
        chooser.setInitialDirectory(new File(Settings.getProperty(SettingsConstant.SCHEMA_PATH)));
        File file = chooser.showSaveDialog(LayoutRepository.getMainStage());
        if (file != null)
            viewModel.saveScript(file);
    }

    private void reload() {
        try {
            viewModel.reload();
        } catch (ObjectNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(LayoutRepository.getMainStage());
            alert.setTitle("Warning");
            alert.setHeaderText("The object had been deleted from database");
            alert.setContentText("Do you want to delete it from your tree?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                viewModel.deleteCurrentItem();
            }
        }
    }
}
