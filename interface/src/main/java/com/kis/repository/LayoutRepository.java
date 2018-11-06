package com.kis.repository;

import com.kis.MainApp;
import com.kis.connection.ConnectionRepository;
import com.kis.view.ConnectionView;

import com.kis.view.RootView;
import com.kis.view.SettingsView;
import com.kis.view.TreeNodeView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class LayoutRepository {

    private static Stage mainStage;

    private static Stage connectionLayout;

    private static Stage settingsLayout;

    private static Alert connectionError;

    private static Alert aboutLayout;

    public static Alert getAboutLayout() {
        if(aboutLayout == null) {
            createAboutLayout();
        }
        return aboutLayout;
    }

    public static Stage getConnectionLayout() {
        if(connectionLayout == null) {
            createConnectionLayout();
        }
        return connectionLayout;
    }

    public static Alert getConnectionErrorLayout() {
        if(connectionError == null) {
            connectionError();
        }
        return connectionError;
    }


    public static Stage getSettingsLayout() {
        if(settingsLayout == null) {
            createSettingsLayout();
        }
        return settingsLayout;
    }

    private static void createAboutLayout() {
        aboutLayout = new Alert(Alert.AlertType.INFORMATION);
        aboutLayout.setTitle("About");
        aboutLayout.setHeaderText(null);
        aboutLayout.setContentText("It'GrammarConstants an educational project of IT-company DB Best Technologies, written by Java-student Kis Yaroslav.");

    }

    private static void createConnectionLayout() {
        try {


            connectionLayout = new Stage();
            connectionLayout.setResizable(false);
            connectionLayout.initOwner(mainStage);
            connectionLayout.initModality(Modality.WINDOW_MODAL);

            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(c -> new ConnectionView());
            loader.setLocation(LayoutRepository.class.getClassLoader().getResource("com/kis/view/connection.fxml"));
            AnchorPane connectionWindow = loader.load();
            connectionLayout.setTitle("Connection parameters");
            connectionLayout.setScene(new Scene(connectionWindow));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createRootLayout() {
        try {
            mainStage.setTitle("KisYProject");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LayoutRepository.class.getClassLoader().getResource("com/kis/view/root.fxml"));
            loader.setControllerFactory(c -> new RootView());
            BorderPane rootLayout = loader.load();

            mainStage.setScene(new Scene(rootLayout));
            treeLayout(rootLayout);
            mainStage.show();
            ConnectionRepository.loadRoot();

            LayoutRepository.getConnectionLayout().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void treeLayout(BorderPane rootLayout) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(c -> new TreeNodeView());
            loader.setLocation(LayoutRepository.class.getClassLoader().getResource("com/kis/view/main.fxml"));
            AnchorPane pane = loader.load();
            rootLayout.setCenter(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createSettingsLayout() {
        try {

            settingsLayout = new Stage();
            settingsLayout.setResizable(false);
            settingsLayout.setTitle("Settings");

            settingsLayout.initOwner(mainStage);
            settingsLayout.initModality(Modality.WINDOW_MODAL);

            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(c -> new SettingsView());
            loader.setLocation(LayoutRepository.class.getClassLoader().getResource("com/kis/view/settings.fxml"));
            AnchorPane connectionWindow = loader.load();

            settingsLayout.setScene(new Scene(connectionWindow));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectionError() {
        connectionError = new Alert(Alert.AlertType.WARNING);
        connectionError.setResizable(false);
        connectionError.initOwner(LayoutRepository.getConnectionLayout());
        connectionError.setTitle("Warning");
        connectionError.setHeaderText("Can't connect to the database");
        connectionError.setContentText("Change values of the connection");

    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        LayoutRepository.mainStage = mainStage;
    }
}
