package com.kis.view;

import com.kis.constant.ConnectionObjectConstant;
import com.kis.domain.TreeNode;
import com.kis.service.NodeStringConverter;
import com.kis.viewmodel.ConnectionViewModel;
import com.kis.viewmodel.ViewModelRepository;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class ConnectionView {

    @FXML private PasswordField password;

    @FXML private ComboBox<TreeNode> server;

    @FXML private ComboBox<TreeNode> port;

    @FXML private ComboBox<TreeNode> schema;

    @FXML private ComboBox<TreeNode> user;

    @FXML private ProgressIndicator indicator;

    @FXML private Button connectionButton;

    private ConnectionViewModel viewModel;

    public ConnectionView() {
        this.viewModel = new ConnectionViewModel();
        ViewModelRepository.setConnectionViewModel(viewModel);
    }



    @FXML private void initialize() {

        indicator.visibleProperty().bindBidirectional(viewModel.visibleIndicatorProperty());

        server.getEditor().textProperty().bindBidirectional(viewModel.serverTextAreaProperty());
        port.getEditor().textProperty().bindBidirectional(viewModel.portTextAreaProperty());
        schema.getEditor().textProperty().bindBidirectional(viewModel.schemaTextAreaProperty());
        user.getEditor().textProperty().bindBidirectional(viewModel.userTextAreaProperty());

        server.valueProperty().bindBidirectional(viewModel.serverValueProperty());
        port.valueProperty().bindBidirectional(viewModel.portValueProperty());
        schema.valueProperty().bindBidirectional(viewModel.schemaValueProperty());
        user.valueProperty().bindBidirectional(viewModel.userValueProperty());

        server.disableProperty().bindBidirectional(viewModel.serverDisableProperty());
        port.disableProperty().bindBidirectional(viewModel.portDisableProperty());
        schema.disableProperty().bindBidirectional(viewModel.schemaDisableProperty());
        user.disableProperty().bindBidirectional(viewModel.userDisableProperty());

        server.itemsProperty().bindBidirectional(viewModel.serverListProperty());
        port.itemsProperty().bindBidirectional(viewModel.portListProperty());
        schema.itemsProperty().bindBidirectional(viewModel.schemaListProperty());
        user.itemsProperty().bindBidirectional(viewModel.userListProperty());

        server.converterProperty().bindBidirectional(viewModel.serverConverterProperty());
        port.converterProperty().bindBidirectional(viewModel.portConverterProperty());
        schema.converterProperty().bindBidirectional(viewModel.schemaConverterProperty());
        user.converterProperty().bindBidirectional(viewModel.userConverterProperty());

        connectionButton.disableProperty().bind(Bindings.isEmpty(server.getEditor().textProperty())
                .or(Bindings.isEmpty(port.getEditor().textProperty()))
                .or(Bindings.isEmpty(schema.getEditor().textProperty()))
                .or(Bindings.isEmpty(user.getEditor().textProperty()))
                .or(Bindings.isEmpty(password.textProperty()))
                .or(viewModel.portIncorrectValueProperty()));

        port.styleProperty().bindBidirectional(viewModel.portStyleProperty());


        server.setStyle("-fx-background-color: grey;");
        server.setEditable(true);
        server.setConverter(new NodeStringConverter(ConnectionObjectConstant.SERVER, ConnectionObjectConstant.ROOT));

        port.setStyle("-fx-background-color: grey;");
        port.setEditable(true);
        port.setConverter(new NodeStringConverter(ConnectionObjectConstant.PORT, ConnectionObjectConstant.SERVER));

        schema.setStyle("-fx-background-color: grey;");
        schema.setEditable(true);
        schema.setConverter(new NodeStringConverter(ConnectionObjectConstant.SCHEMA, ConnectionObjectConstant.PORT));

        user.setStyle("-fx-background-color: grey;");
        user.setEditable(true);
        user.setConverter(new NodeStringConverter(ConnectionObjectConstant.USER, ConnectionObjectConstant.SCHEMA));

        password.setStyle("-fx-text-box-border: grey;");
        server.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                return;
            viewModel.serverValueProperty().set(viewModel.serverConverterProperty().get().fromString(server.editorProperty().get().getText()));
            viewModel.setPortList(viewModel.serverValueProperty().get().getChildren());
        });

        port.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                return;
            viewModel.portValueProperty().set(viewModel.portConverterProperty().get().fromString(port.editorProperty().get().getText()));
            viewModel.setSchemaList(viewModel.portValueProperty().get().getChildren());
        });

        schema.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                return;
            viewModel.schemaValueProperty().set(viewModel.schemaConverterProperty().get().fromString(schema.editorProperty().get().getText()));
            viewModel.setUserList(viewModel.schemaValueProperty().get().getChildren());
        });

        password.textProperty().bindBidirectional(viewModel.passwordTextAreaProperty());

        server.getEditor().textProperty().setValue("dev-mysql.cjj06khxetlc.us-west-2.rds.amazonaws.com");
        port.getEditor().textProperty().setValue("3306");
        schema.getEditor().textProperty().setValue("test_mysql_mysql");
        user.getEditor().textProperty().setValue("min_privs");
        password.setText("min_privs");

    }

    @FXML private void connect() {
        viewModel.connect();
    }

}
