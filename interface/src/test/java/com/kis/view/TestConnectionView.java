package com.kis.view;

import com.kis.repository.LayoutRepository;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;

public class TestConnectionView extends ApplicationTest{
    @Override
    public void start(Stage stage) throws Exception {

        LayoutRepository.setMainStage(stage);
        LayoutRepository.createRootLayout();
    }

    @Test
    public void testConnection() throws InterruptedException {

        clickOn("#server").write("dev-mysql.cjj06khxetlc.us-west-2.rds.amazonaws.com");
        clickOn("#port").write("3306");
        clickOn("#schema").write("test_mysql");
        clickOn("#user").write("min_privs");
        clickOn("#password").write("min_privs");
        clickOn("#connect");
        FxAssert.verifyThat("#indicator", Node::isVisible);


    }

    @Test
    public void testValidating() {

        clickOn("#server").write("dev-mysql.cjj06khxetlc.us-west-2.rds.amazonaws.com");
        clickOn("#port").write("3306");
        clickOn("#user").write("min_privs");
        clickOn("#password").write("min_privs");
        clickOn("#connect");
        Assert.assertEquals(LayoutRepository.getConnectionErrorLayout().isShowing(), true);

    }

    @After
    public void waitForConnection() throws InterruptedException {
        Thread.sleep(7000);
    }

}
