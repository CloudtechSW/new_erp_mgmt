package com.example.new_erp_mgmt;

import com.example.new_erp_mgmt.Controllers.User.LoginController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/login.fxml"));
        final Parent root = (Parent) loader.load();
        LoginController lc = loader.getController();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
        stage.setScene(scene);
        root.setOpacity(0);
        stage.show();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.2), root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        lc.setStage(stage);
    }
    public static void main(String[] args){
        launch(args);
    }
}
