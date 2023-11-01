package com.example.new_erp_mgmt.Controllers.User;

import com.example.new_erp_mgmt.App;
import com.example.new_erp_mgmt.Database.DBMysql;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField txtUsername;
    public TextField txt_Pwd;
    public Button btn_login;
    public Button btn_close;
    public Label lbl_wtsap;
    public Label lbl_insta;
    public Label lbl_fb;
    public Label lblMsg;
    private Stage stage;
    public static String user="";
    public static String userType="";
    private final Stage StgMDI = new Stage(StageStyle.UTILITY);
    Scene scene_MDI = null;
    DBMysql db = new DBMysql();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        Listeners();
    }
    private void Listeners(){

        txtUsername.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()== KeyCode.ENTER){
                    if(txtUsername.getText().equalsIgnoreCase("")){lblMsg.setText("Please Enter Username !!!");}
                    else{txt_Pwd.requestFocus(); lblMsg.setText("");}
                }
            }
        });
        txt_Pwd.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() ==KeyCode.ENTER){
                    if (txt_Pwd.getText().equalsIgnoreCase("")){lblMsg.setText("Please Enter Password");}
                    else{btn_login.requestFocus(); lblMsg.setText("");}
                }
            }
        });
        btn_login.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                btn_login.fire();
            }
        });
    }
    private void login(){
//        try{
//            PreparedStatement ps = db.con.prepareStatement("select * from "+db.schema+".tbl_user where BINARY username ='"+txtUsername.getText()+"' AND BINARY password ='"+txt_Pwd.getText()+"' ");
////            ps.setString(1,txtUsername.getText());
////            ps.setString(2,txt_Pwd.getText());
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()){
//                user= rs.getString("lastname");
//                userType=rs.getString("usertype");
        user = "admin";
        mdi();
        stage.close();
//            }else{
//                lblMsg.setText("invalid Username/Password");
//            }
//        } catch (e)
//            throw new RuntimeException(e);
//        }
    }
    private void mdi(){
        if (!StgMDI.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/MDI.fxml"));
                Parent root = loader.load();
                MDIController mc = loader.getController();
                scene_MDI = new Scene(root);
                StgMDI.setScene(scene_MDI);
                StgMDI.setResizable(false);
                StgMDI.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
                mc.setStage(StgMDI);
                StgMDI.show();
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                fadeTransition.play();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void btnLogin_OnAction(ActionEvent actionEvent) {
//        if(txtUsername.getText().equalsIgnoreCase("")){lblMsg.setText("Please Enter Username !!!");}
//        else if(txt_Pwd.getText().equalsIgnoreCase("")){lblMsg.setText("Please Enter Password!!!");}
//        else{lblMsg.setText("");  }
        login();
    }
    public void btnClose_OnAction(ActionEvent actionEvent) {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
