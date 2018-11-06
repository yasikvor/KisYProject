package com.kis.view;

import com.kis.repository.LayoutRepository;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;

public class TestTreeTreeNodeView extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        LayoutRepository.setMainStage(stage);
        LayoutRepository.createRootLayout();
    }

    @Before
    public void testConnection() {
        Platform.runLater(() ->  LayoutRepository.getConnectionLayout().close());
    }

    @Test
    public void testMenuItemNew() {
        clickOn("#menu").clickOn("#new");
        Assert.assertEquals(LayoutRepository.getConnectionLayout().isShowing(), true);
        LayoutRepository.getConnectionLayout().close();

    }
}
