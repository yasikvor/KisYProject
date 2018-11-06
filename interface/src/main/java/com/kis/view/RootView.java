package com.kis.view;

import com.kis.connection.ConnectionRepository;
import com.kis.constant.SettingsConstant;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import com.kis.model.NodeModel;
import com.kis.model.ProjectModel;
import com.kis.repository.LayoutRepository;
import com.kis.settings.Settings;
import com.kis.viewmodel.RootViewModel;
import com.kis.viewmodel.ViewModelRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

public class RootView {

    private RootViewModel viewModel;

    public RootView() {
        viewModel = new RootViewModel();
        ViewModelRepository.setRootViewModel(viewModel);
    }

    @FXML private MenuItem newProject;

    @FXML private MenuItem openProject;

    @FXML private MenuItem saveProject;

    @FXML private MenuItem closeProject;

    @FXML private MenuItem connect;

    @FXML private MenuItem loadFullDatabase;

    @FXML private MenuItem reload;

    @FXML private MenuItem exit;

    @FXML private MenuItem createSql;

    @FXML void initialize() {
        newProject.disableProperty().bindBidirectional(viewModel.newProjectDisabledProperty());
        openProject.disableProperty().bindBidirectional(viewModel.openProjectDisabledProperty());
        saveProject.disableProperty().bindBidirectional(viewModel.saveProjectDisabledProperty());
        closeProject.disableProperty().bindBidirectional(viewModel.closeProjectDisabledProperty());
        connect.disableProperty().bindBidirectional(viewModel.connectDisabledProperty());
        reload.disableProperty().bindBidirectional(viewModel.reloadDisabledProperty());
        createSql.disableProperty().bindBidirectional(viewModel.createSqlDisabledProperty());
        loadFullDatabase.disableProperty().bindBidirectional(viewModel.loadAllDatabaseDisabledProperty());

        newProject.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        openProject.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        exit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.CONTROL_DOWN));
        saveProject.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        loadFullDatabase.setOnAction(event -> {
            ViewModelRepository.getTreeNodeViewModel().loadFullDatabase();
            ViewModelRepository.getTreeNodeViewModel().textAreaProperty().set("");
            ViewModelRepository.getTreeNodeViewModel().tableVisibleProperty().set(false);
        });

        LayoutRepository.getMainStage().setOnCloseRequest(event -> {
            exitProgram();
        });

        reload.setOnAction(event -> {
            try {
                ViewModelRepository.getTreeNodeViewModel().reload();
            } catch (ObjectNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initOwner(LayoutRepository.getMainStage());
                alert.setTitle("Warning");
                alert.setHeaderText("The object had been deleted from database");
                alert.setContentText("Do you want to delete it from your tree?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ViewModelRepository.getTreeNodeViewModel().deleteCurrentItem();
                }
            }
        });

        createSql.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQl files (*.sql)", "*.sql");
            chooser.getExtensionFilters().add(extFilter);
            chooser.setTitle("Save script");
            chooser.setInitialDirectory(new File(Settings.getProperty(SettingsConstant.SCHEMA_PATH)));
            File file = chooser.showSaveDialog(LayoutRepository.getMainStage());
            if(file != null)
                ViewModelRepository.getTreeNodeViewModel().saveScript(file);
        });

        loadFullDatabase.setDisable(true);
        reload.setDisable(true);
        createSql.setDisable(true);
        saveProject.disableProperty().setValue(true);
        closeProject.disableProperty().setValue(true);
        connect.disableProperty().setValue(true);


    }

    @FXML
    private void connect() {
        ViewModelRepository.getTreeNodeViewModel().selectedItemProperty().set(null);
        ViewModelRepository.getTreeNodeViewModel().enterPassword();
    }


    @FXML
    public void newConnection() {
        ConnectionRepository.loadRoot();
        ViewModelRepository.getConnectionViewModel().setServerList(ConnectionRepository.getRoot().getChildren());
        LayoutRepository.getConnectionLayout().show();
    }

    @FXML
    private void exitProgram() {
        if(NodeModel.getRoot() != null)
            saveProject();
        System.exit(1);
    }

    @FXML
    private void closeProject() {
        saveProject();
        connect.disableProperty().setValue(true);
        try {
            ViewModelRepository.getTreeNodeViewModel().closeProject();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showSettings() {
        LayoutRepository.getSettingsLayout().show();
    }

    @FXML
    public void saveProject() {
        if(ProjectModel.getProjectFile() != null) {
            ViewModelRepository.getTreeNodeViewModel().saveProject(ProjectModel.getProjectFile());
            return;
        }
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setTitle("Save project");
        chooser.setInitialDirectory(new File(Settings.getProperty(SettingsConstant.PROJECT_PATH)));
        File file = chooser.showSaveDialog(LayoutRepository.getMainStage());
        if(file != null)
            ViewModelRepository.getTreeNodeViewModel().saveProject(file);
    }

    @FXML
    private void showAbout() {
        LayoutRepository.getAboutLayout().show();
    }

    @FXML
    public void openProject() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setTitle("Open project");

        chooser.setInitialDirectory(new File(Settings.getProperty(SettingsConstant.PROJECT_PATH)));

        File file = chooser.showOpenDialog(LayoutRepository.getMainStage());
        if(file == null)
            return;
        try {
            ViewModelRepository.getTreeNodeViewModel().openProject(file);
            ViewModelRepository.getRootViewModel().connectDisabledProperty().set(false);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("XML-file is not valid");
            alert.initOwner(LayoutRepository.getMainStage());
            alert.show();
            e.printStackTrace();
        }
    }
}
