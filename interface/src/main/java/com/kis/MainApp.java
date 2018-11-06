package com.kis;


import com.kis.repository.LayoutRepository;
import com.kis.view.TreeNodeView;
import javafx.application.Application;

import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.util.Optional;


public class MainApp extends Application {

    public MainApp() {

    }

    @Override
    public void start(Stage stage) {
        LayoutRepository.setMainStage(stage);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        LayoutRepository.createRootLayout();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        System.exit(1);
    }
}