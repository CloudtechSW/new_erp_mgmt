package com.example.new_erp_mgmt.Controllers.User;

import com.example.new_erp_mgmt.App;
import com.example.new_erp_mgmt.Controllers.Masters.Brand.BrandController;
import com.example.new_erp_mgmt.Controllers.Masters.Category.CategoryController;
import com.example.new_erp_mgmt.Controllers.Masters.Department.DepartmentController;
import com.example.new_erp_mgmt.Controllers.Masters.Designation.DesignationController;
import com.example.new_erp_mgmt.Controllers.Masters.ItemMaster.ItemMasterController;
import com.example.new_erp_mgmt.Controllers.Masters.SupplierMaster.SupplierMasterController;
import com.example.new_erp_mgmt.Controllers.Masters.Tax.TaxController;
import com.example.new_erp_mgmt.Controllers.Masters.Unit.UnitController;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MDIController implements Initializable {

    public Label lblUser;
    public MenuItem menu_Brand;
    public MenuItem menu_catgy;
    public MenuItem menu_dept;
    public MenuItem menu_desig;
    public MenuItem menu_item;
    public MenuItem menu_tax;
    public MenuItem menu_warehouse;
    public MenuItem menu_supplier;
    public MenuItem menu_unit;
    public Button btnDashboard;
    public Button btnSales;
    public Button btnPurchase;
    public Button btnLedger;
    public Button btnItems;
    public Button btnSupplier;
    public Button btnEmploy;
    public Button btnClients;

    private Stage stage_brand = new Stage(StageStyle.TRANSPARENT);
    Scene scene_brand = null;

    private Stage stage_ctgry = new Stage(StageStyle.TRANSPARENT);
    Scene scene_ctgry = null;
    private Stage stage_dept = new Stage(StageStyle.TRANSPARENT);
    Scene scene_dept = null;
    private Stage stage_desig = new Stage(StageStyle.TRANSPARENT);
    Scene scene_desig = null;
    private Stage stage_item = new Stage(StageStyle.UTILITY);
    Scene scene_item = null;
    private Stage stage_supplier = new Stage(StageStyle.UTILITY);
    Scene scene_supplier = null;
    private Stage stage_tax = new Stage(StageStyle.TRANSPARENT);
    Scene scene_tax = null;
    private Stage stage_unit = new Stage(StageStyle.TRANSPARENT);
    Scene scene_unit = null;
    private Stage stage_wh = new Stage(StageStyle.TRANSPARENT);
    Scene scene_wh = null;
    private Stage stage_MDI;
    LoginController lc = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("user name == "+lc.user);
        lblUser.setText("Hi "+lc.user);
    }
    public void MenuBrand_OnClick(ActionEvent actionEvent) {
        if (!stage_brand.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Master/brand.fxml"));
                Parent root = (Parent) loader.load();
                BrandController bc = loader.getController();
                scene_brand = new Scene(root);
                stage_brand.setScene(scene_brand);
                stage_brand.setResizable(false);
                stage_brand.setTitle("Brand Master");
                stage_brand.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
                bc.setStage(stage_brand);
                stage_brand.show();
                setFadeInTransition(root);
            } catch (IOException ex) {
                Alert alt = new Alert(Alert.AlertType.ERROR,ex.getMessage(), ButtonType.OK);
                alt.showAndWait();
                throw new RuntimeException(ex);

            }
        }
    }
    public void MenuCatgy_OnClick(ActionEvent actionEvent) {
        if (!stage_ctgry.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Master/category.fxml"));
                Parent root = (Parent) loader.load();
                CategoryController cc = loader.getController();
                scene_ctgry = new Scene(root);
                stage_ctgry.setScene(scene_ctgry);
                stage_ctgry.setResizable(false);
                stage_ctgry.setTitle("Category Master");
                stage_ctgry.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
                cc.setStage(stage_ctgry);
                stage_ctgry.show();
                setFadeInTransition(root);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void menuDept_onClick(ActionEvent actionEvent){
        if (!stage_dept.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Master/department.fxml"));
                Parent root = (Parent) loader.load();
                DepartmentController cc = loader.getController();
                scene_dept = new Scene(root);
                stage_dept.setScene(scene_dept);
                stage_dept.setResizable(false);
                stage_dept.setTitle("Department Master");
                stage_dept.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
                cc.setStage(stage_dept);
                stage_dept.show();
                setFadeInTransition(root);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void menuDesig_onClick(ActionEvent actionEvent){
        if(!stage_desig.isShowing()){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Master/designation.fxml"));
                Parent root =loader.load();
                DesignationController Dc = loader.getController();
                scene_desig = new Scene(root);
                stage_desig.setScene(scene_desig);
                stage_desig.setResizable(false);
                stage_desig.setTitle("Designation Master");
                stage_desig.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
                Dc.setStage(stage_desig);
                stage_desig.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void menuItem_onClick(ActionEvent actionEvent){
//        if(!stage_item.isShowing()){
//            try{
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Masters/ItemMaster/itemMaster.fxml"));
//                Parent root = loader.load();
//                ItemMasterController imc =loader.getController();
//                scene_item = new Scene(root);
//                stage_item.setScene(scene_item);
//                stage_item.setResizable(false);
//                imc.setStage(stage_item);
//                stage_item.show();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//        }
    }
    public void menuSupplier_onClick(ActionEvent actionEvent){
//        if(!stage_supplier.isShowing()){
//            try{
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Masters/SupplierMaster/supplierMaster.fxml"));
//                Parent root = loader.load();
//                SupplierMasterController sc = loader.getController();
//                scene_supplier = new Scene(root);
//                stage_supplier.setScene(scene_supplier);
//                stage_supplier.setResizable(false);
//                stage_supplier.setTitle("Supplier");
//                stage_supplier.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
//                sc.setStage(stage_supplier);
//                stage_supplier.show();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
    public void menuTax_onClick(ActionEvent actionEvent){
//        if(!stage_tax.isShowing()){
//            try{
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Masters/taxMaster.fxml"));
//                Parent root = loader.load();
//                TaxController tc = loader.getController();
//                scene_tax = new Scene(root);
//                stage_tax.setScene(scene_tax);
//                stage_tax.setResizable(false);
//                stage_tax.setTitle("Tax Master");
//                stage_tax.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
//                tc.setStage(stage_tax);
//                stage_tax.show();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
    public void menuUnit_onClick(ActionEvent actionEvent){
//        if(!stage_unit.isShowing()){
//            try{
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Masters/unit.fxml"));
//                Parent root = loader.load();
//                UnitController uc = loader.getController();
//                scene_unit = new Scene(root);
//                stage_unit.setScene(scene_unit);
//                stage_unit.setResizable(false);
//                stage_unit.setTitle("Unit Master");
//                stage_unit.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
//                uc.setStage(stage_unit);
//                stage_unit.show();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
    public void menuWH_onClick(ActionEvent actionEvent){}
    public void btnDashboardOnAction(ActionEvent actionEvent){}
    public void btnSalesOnAction(ActionEvent actionEvent) {}
    public void btnPurchaseOnAction(ActionEvent actionEvent) {}
    public void btnLedgerOnAction(ActionEvent actionEvent) {}
    public void btnClientsOnAction(ActionEvent actionEvent) {}
    public void btnEmployOnAction(ActionEvent actionEvent) {}
    public void btnSupplierOnAction(ActionEvent actionEvent) {}
    public void btnItemsOnAction(ActionEvent actionEvent) {}
    public void btnLogoutOnAction(ActionEvent actionEvent) {}
    public void setStage(Stage stgMDI) {this.stage_MDI = stgMDI;}
    public void setFadeInTransition(Parent root) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
}

