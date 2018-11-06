package com.kis.viewmodel;

import com.kis.connection.ConnectionRepository;
import com.kis.domain.TreeNode;
import com.kis.model.*;
import com.kis.printer.PrinterManager;
import com.kis.repository.LayoutRepository;
import com.kis.task.LoadNodeTask;
import com.kis.task.ShowRootTask;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.layout.Background;
import javafx.util.StringConverter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ConnectionViewModel {

    public ConnectionViewModel() {

        LayoutRepository.getConnectionLayout().setOnCloseRequest(event -> {
            portStyle.set("-fx-background-color: grey;");
            serverTextArea.set("");
            portTextArea.set("");
            schemaTextArea.set("");
            userTextArea.set("");
            passwordTextArea.set("");
        });

        serverValue.addListener(((observable, oldValue, newValue) -> {

            if(newValue == null)
                return;

            this.setPortList(newValue.getChildren());
            this.setSchemaList(null);
            this.setUserList(null);


        }));

        portValue.addListener(((observable, oldValue, newValue) -> {
            if(newValue == null)
                return;
            this.setSchemaList(newValue.getChildren());
            this.setUserList(null);

        }));

        portTextArea.addListener((observable, oldValue, newValue) -> {
            if(portTextArea.get().equals("")) {
                portStyle.set("-fx-background-color: gray;");
                return;
            }
            if(Pattern.matches("[0-9]{1,5}", portTextArea.get())) {
                if(Integer.parseInt(portTextArea.get()) > 65536) {
                    portStyle.set("-fx-background-color: red;");
                    portIncorrectValue.set(true);
                }
                else {
                    portStyle.set("-fx-background-color: gray;");
                    portIncorrectValue.set(false);
                }
            }
            else {
                portStyle.set("-fx-background-color: red;");
                portIncorrectValue.set(true);
            }
        });

        schemaValue.addListener(((observable, oldValue, newValue) -> {

            if(newValue == null)
                return;

            this.setUserList(newValue.getChildren());
        }));
        serverList.get().addAll(ConnectionRepository.getRoot().getChildren());

    }

    private final SimpleBooleanProperty visibleIndicator = new SimpleBooleanProperty();

    private final SimpleStringProperty passwordTextArea = new SimpleStringProperty();

    private final SimpleObjectProperty<ObservableList<TreeNode>> serverList = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private final SimpleObjectProperty<ObservableList<TreeNode>> portList = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private final SimpleObjectProperty<ObservableList<TreeNode>> schemaList = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private final SimpleObjectProperty<ObservableList<TreeNode>> userList = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private final SimpleObjectProperty<TreeNode> serverValue = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<TreeNode> portValue = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<TreeNode> schemaValue = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<TreeNode> userValue = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<StringConverter<TreeNode>> serverConverter = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<StringConverter<TreeNode>> portConverter = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<StringConverter<TreeNode>> schemaConverter = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<StringConverter<TreeNode>> userConverter = new SimpleObjectProperty<>();

    private final SimpleStringProperty serverTextArea = new SimpleStringProperty();

    private final SimpleStringProperty portTextArea = new SimpleStringProperty();

    private final SimpleStringProperty schemaTextArea = new SimpleStringProperty();

    private final SimpleStringProperty userTextArea = new SimpleStringProperty();

    private final SimpleBooleanProperty serverDisable = new SimpleBooleanProperty();

    private final SimpleBooleanProperty portDisable = new SimpleBooleanProperty();

    private final SimpleBooleanProperty schemaDisable = new SimpleBooleanProperty();

    private final SimpleBooleanProperty userDisable = new SimpleBooleanProperty();

    private final SimpleStringProperty portStyle = new SimpleStringProperty();

    private final SimpleObjectProperty<Background> portBackground = new SimpleObjectProperty<>();

    private final SimpleBooleanProperty portIncorrectValue = new SimpleBooleanProperty();

    public void connect() {



        ExecutorService service = Executors.newSingleThreadExecutor();

        Task task = new ConnectionTask();

        task.setOnFailed(event -> {
            visibleIndicator.set(false);
            LayoutRepository.getConnectionErrorLayout().show();
        });
        task.setOnSucceeded(event -> {
            if(LayoutRepository.getMainStage().getTitle().equals("KisYProject")) {
                LayoutRepository.getMainStage().setTitle("KisYProject [New project]");

            }
            ViewModelRepository.getTreeNodeViewModel().selectedItemProperty().set(null);
            ViewModelRepository.getRootViewModel().connectDisabledProperty().set(true);
            ViewModelRepository.getRootViewModel().loadAllDatabaseDisabledProperty().set(false);
            ViewModelRepository.getTreeNodeViewModel().loadAllDatabaseMenuItemDisabledProperty().set(false);

            if(NodeModel.getRootTreeItem() == null) {
                Task loadRoot = new ShowRootTask();
                Platform.runLater(loadRoot);

            }
            else if(ViewModelRepository.getTreeNodeViewModel().selectedItemProperty().get() != null) {
                Task loadRoot = new LoadNodeTask(ViewModelRepository.getTreeNodeViewModel().selectedItemProperty().get());
                Platform.runLater(loadRoot);
            }

            LayoutRepository.getConnectionLayout().close();
            serverTextArea.set("");
            portTextArea.set("");
            schemaTextArea.set("");
            userTextArea.set("");
            passwordTextArea.set("");
        });
        service.submit(task);

    }


    public SimpleObjectProperty<StringConverter<TreeNode>> serverConverterProperty() {
        return serverConverter;
    }

    public SimpleObjectProperty<StringConverter<TreeNode>> portConverterProperty() {
        return portConverter;
    }

    public SimpleObjectProperty<StringConverter<TreeNode>> schemaConverterProperty() {
        return schemaConverter;
    }

    public SimpleObjectProperty<StringConverter<TreeNode>> userConverterProperty() {
        return userConverter;
    }

    public SimpleStringProperty passwordTextAreaProperty() {
        return passwordTextArea;
    }

    public SimpleObjectProperty<ObservableList<TreeNode>> serverListProperty() {
        return serverList;
    }

    public SimpleObjectProperty<ObservableList<TreeNode>> portListProperty() {
        return portList;
    }

    public void setPortList(List<TreeNode> portList) {
        this.setChildren(portList, portTextArea, this.portList);
    }

    public SimpleObjectProperty<ObservableList<TreeNode>> schemaListProperty() {
        return schemaList;
    }

    public void setSchemaList(List<TreeNode> schemaList) {
        this.setChildren(schemaList, schemaTextArea, this.schemaList);
    }

    public SimpleObjectProperty<ObservableList<TreeNode>> userListProperty() {
        return userList;
    }

    public void setUserList(List<TreeNode> userList) {
        this.setChildren(userList, userTextArea, this.userList);
    }

    public void setServerList(List<TreeNode> serverList) {
        this.setChildren(serverList, serverTextArea, this.serverList);
    }

    public SimpleStringProperty serverTextAreaProperty() {
        return serverTextArea;
    }

    public SimpleStringProperty portTextAreaProperty() {
        return portTextArea;
    }

    public SimpleStringProperty schemaTextAreaProperty() {
        return schemaTextArea;
    }

    public SimpleStringProperty userTextAreaProperty() {
        return userTextArea;
    }

    public SimpleObjectProperty<TreeNode> serverValueProperty() {
        return serverValue;
    }

    public SimpleObjectProperty<TreeNode> portValueProperty() {
        return portValue;
    }

    public SimpleObjectProperty<TreeNode> schemaValueProperty() {
        return schemaValue;
    }

    public SimpleObjectProperty<TreeNode> userValueProperty() {
        return userValue;
    }

    public SimpleBooleanProperty visibleIndicatorProperty() {
        return visibleIndicator;
    }

    public SimpleBooleanProperty portIncorrectValueProperty() {
        return portIncorrectValue;
    }

    public SimpleBooleanProperty serverDisableProperty() {
        return serverDisable;
    }


    public SimpleBooleanProperty portDisableProperty() {
        return portDisable;
    }

    public SimpleBooleanProperty schemaDisableProperty() {
        return schemaDisable;
    }


    public SimpleBooleanProperty userDisableProperty() {
        return userDisable;
    }

    public SimpleObjectProperty<Background> portBackgroundProperty() {
        return portBackground;
    }

    public SimpleStringProperty portStyleProperty() {
        return portStyle;
    }

    private void setChildren(List<TreeNode> children, SimpleStringProperty textArea, SimpleObjectProperty<ObservableList<TreeNode>> comboBoxProperty) {
        String text = textArea.get();
        comboBoxProperty.get().clear();
        if(children != null && children.size() != 0)
            comboBoxProperty.get().addAll(children);
        textArea.set(text);
    }

    class ConnectionTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            visibleIndicator.set(true);
            if(serverDisable.get()) {
                ConnectionModel.setConnection(passwordTextArea.get());
            }
            else {
                ConnectionModel.setConnection(serverTextArea.get(), portTextArea.get(), schemaTextArea.get(), userTextArea.get(), passwordTextArea.get());
                ConnectionRepository.saveConnection();
            }


            visibleIndicator.set(false);
            return null;
        }
    }
}
