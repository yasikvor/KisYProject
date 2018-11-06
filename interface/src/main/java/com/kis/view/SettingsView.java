package com.kis.view;

import com.kis.MainApp;
import com.kis.constant.SettingsConstant;
import com.kis.repository.LayoutRepository;
import com.kis.settings.Settings;
import com.kis.viewmodel.SettingsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class SettingsView {

    private SettingsViewModel viewModel;

    public SettingsView() {
        viewModel = new SettingsViewModel();
    }

    @FXML
    private TextField projectPath;

    @FXML
    private TextField schemaPath;

    @FXML
    private TextField logPath;

    @FXML
    private Button changeProjectPathButton;

    @FXML
    private Button changeSchemaPathButton;

    @FXML
    private Button changeLogPathButton;

    @FXML
    private Button saveChanges;

    @FXML
    private void initialize() {

        projectPath.setEditable(false);
        schemaPath.setEditable(false);
        logPath.setEditable(false);

        projectPath.textProperty().bindBidirectional(viewModel.projectPathProperty());

        schemaPath.textProperty().bindBidirectional(viewModel.schemaPathProperty());

        logPath.textProperty().bindBidirectional(viewModel.logPathProperty());

        changeLogPathButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            chooser.getExtensionFilters().add(extFilter);
            chooser.setTitle("New logger path");
            String path = logPath.getText();
            chooser.setInitialDirectory(new File(path.substring(0, path.lastIndexOf("\\"))));
            File file = chooser.showSaveDialog(LayoutRepository.getMainStage());
            if(file != null) {
                logPath.setText(file.getAbsolutePath());
            }

        });

        changeProjectPathButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();

            chooser.setTitle("New project folder");
            String path = projectPath.getText();
            //chooser.setInitialDirectory(new File(path.substring(0, path.lastIndexOf("\\"))));
            chooser.setInitialDirectory(new File(path));
            File file = chooser.showDialog(LayoutRepository.getMainStage());
            if(file != null) {
                projectPath.setText(file.getAbsolutePath());
            }

        });

        changeSchemaPathButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();

            chooser.setTitle("New schema folder");
            String path = schemaPath.getText();
            chooser.setInitialDirectory(new File(path));
            File file = chooser.showDialog(LayoutRepository.getMainStage());
            if(file != null) {
                schemaPath.setText(file.getAbsolutePath());
            }
        });

        LayoutRepository.getSettingsLayout().setOnCloseRequest(event -> viewModel.closeWindow());
        saveChanges.setOnAction(event -> {
            viewModel.saveSetting();
            LayoutRepository.getSettingsLayout().close();
        });
    }


}
