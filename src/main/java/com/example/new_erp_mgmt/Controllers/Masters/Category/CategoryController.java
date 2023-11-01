package com.example.new_erp_mgmt.Controllers.Masters.Category;


import com.example.new_erp_mgmt.Controllers.User.LoginController;
import com.example.new_erp_mgmt.Database.DBMysql;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryController implements Initializable {
    public TextField txtID;
    public TextField txtName;
    public TextField txtRemarks;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnClear;
    public Button btnDelete;
    public Button btnActive;
    public Button btnPrint;
    public CheckBox chkActive;
    public TableView<objCat> tblView;
    public TableColumn colSlno;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colRemarks;
    public TableColumn colStatus;
    public Button btnClose;
    private Stage stage_Catgry;
    ObservableList <objCat> tblData = FXCollections.observableArrayList();
    DBMysql db = new DBMysql();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    DateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chkActive.setSelected(true);
        setTable();
        clear();
        listeners();
    }
    private void clear(){
        btnSave.setVisible(true);
        btnClear.setVisible(true);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        btnActive.setVisible(false);
        txtName.clear();
        txtRemarks.clear();
        Platform.runLater(()->txtName.requestFocus());
        setCode();
        populateTable();
    }
    private void setCode(){
        String id = "0";
        try{
            Statement st = db.con.createStatement();
            ResultSet rs = st.executeQuery("select count(code), max(code) from "+db.schema+".tbl_ctgry");
            if(rs.next()){
                id  = rs.getString(1);
                int count =  rs.getInt(1);
                if(count==0){id="0";} else{
                    String str = rs.getString(2);
                    String [] part = str.split("(?<=\\D)(?=\\d)");
                    id= part[1];
                }
                int pr = Integer.parseInt(id)+1;
                String no = String.format("%03d", pr);
                txtID.setText("CTG"+no);
            }
        } catch (Exception ex) {
            Alert alt = new Alert(Alert.AlertType.ERROR,ex.getMessage(),ButtonType.OK);
            alt.show();
            throw new RuntimeException(ex);
        }
    }
    private void setTable(){
        tblData.clear();
        colID.setCellValueFactory(new PropertyValueFactory<objCat,String>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<objCat,String>("name"));
        colRemarks.setCellValueFactory(new PropertyValueFactory<objCat,String>("remarks"));
        colStatus.setCellValueFactory(new PropertyValueFactory<objCat,String>("status"));
        colSlno.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<objCat, objCat>, ObservableValue<objCat>>() {
            @Override
            public ObservableValue<objCat> call(TableColumn.CellDataFeatures<objCat, objCat> p) {
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });
        colSlno.setCellFactory(new Callback<TableColumn<objCat, objCat>, TableCell<objCat, objCat>>() {
            @Override public TableCell<objCat, objCat> call(TableColumn<objCat, objCat> param) {
                return new TableCell<objCat, objCat>() {
                    @Override
                    protected void updateItem(objCat item, boolean empty) {
                        super.updateItem(item, empty);
                        if (this.getTableRow() != null && item != null) {setText(this.getTableRow().getIndex()+1+"");}
                        else {setText("");}
                    }
                };
            }
        });
        colSlno.setSortable(false);
        tblView.setItems(tblData);
    }
    private void populateTable(){
        tblData.clear();
        try{
            Statement st = db.con.createStatement();
            ResultSet rs;
            if(chkActive.isSelected()){rs  = st.executeQuery("select * from " + db.schema + ".tbl_ctgry where status = 1");}
            else{rs = st.executeQuery("select * from " + db.schema + ".tbl_ctgry ");}
            while (rs.next()) {
                String sts;
                if(rs.getString("status").equalsIgnoreCase("1")) {sts = "Active";}else{sts ="In Active";}
                tblData.add(new objCat(rs.getString("code"), rs.getString("name"), rs.getString("remarks"),sts));
            }
            st.close();
        } catch (SQLException ex) {
            Alert alt = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alt.showAndWait();
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void listeners(){
        txtName.addEventFilter(KeyEvent.KEY_RELEASED,new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent t) {
                if(t.getCode() == KeyCode.ENTER){txtRemarks.requestFocus();}
            }
        });
        txtRemarks.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    if(btnSave.isVisible()){btnSave.requestFocus();}
                    if(btnUpdate.isVisible()){btnUpdate.requestFocus();}
                }
            }
        });
        chkActive.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                clear();
            }
        });
        tblView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.PRIMARY && !tblView.getSelectionModel().isEmpty()){
                    fetch_TableData(tblView.getSelectionModel().getSelectedItem());
                }
            }
        });
    }
    private void fetch_TableData(objCat obj) {
        btnSave.setVisible(false);
        btnClear.setVisible(false);
        btnUpdate.setVisible(true);
        if(obj.getStatus() == "In Active"){btnDelete.setVisible(false); btnActive.setVisible(true);}
        else{btnActive.setVisible(false); btnDelete.setVisible(true);}
        txtID.setText(obj.getCode());
        txtName.setText(obj.getName());
        txtRemarks.setText(obj.getRemarks());
    }
    private void SaveValidation(){
        if ( txtID.getText().equalsIgnoreCase("") || txtName.getText().equalsIgnoreCase("")){
            Alert alt = new Alert(Alert.AlertType.ERROR,"Please Enter Category Name",ButtonType.OK);
            alt.showAndWait();
            if(alt.getResult()==ButtonType.OK){txtName.requestFocus();}
        }else {
            Alert alt1 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure, You want to save this Category ?",ButtonType.YES,ButtonType.NO);
            alt1.showAndWait();
            if (alt1.getResult() == ButtonType.YES){SaveCategory();}else{txtName.requestFocus();}
        }
    }
    private void SaveCategory(){
        try{
            Statement st = db.con.createStatement();
            int rs = st.executeUpdate("insert into "+db.schema+".tbl_ctgry values(null,'"+txtID.getText()+"','"+txtName.getText().toUpperCase().trim()+"','"+txtRemarks.getText()+"',1," +
                    " '"+ LoginController.user +"','"+dateFormat.format(date)+"',null,null,null,null)");
            if(rs == 1){
                Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Category Saved Successfully!",ButtonType.OK);
                alt.showAndWait();
                if(alt.getResult()==ButtonType.OK){clear();}
            }
            st.close();
        } catch (Exception ex) {
            Alert alt1 = new Alert(Alert.AlertType.ERROR,ex.getMessage(),ButtonType.OK);
            alt1.showAndWait();
            throw new RuntimeException(ex);
        }
    }
    private void UpdateValidation(){
        if(tblView.getSelectionModel().isEmpty()){
            Alert alt1 = new Alert(Alert.AlertType.ERROR ,"Please Select a Category from Table",ButtonType.OK);
            alt1.showAndWait();
            if (alt1.getResult() == ButtonType.OK){tblView.requestFocus();}
        }else if(txtID.getText().equalsIgnoreCase("") || txtName.getText().equalsIgnoreCase("") ) {
            Alert alt2 = new Alert(Alert.AlertType.ERROR, "Please Enter Category Name!!", ButtonType.OK);
            alt2.showAndWait();
            if (alt2.getResult() == ButtonType.OK) {txtName.requestFocus();}
        }else if(Objects.equals(tblView.getSelectionModel().getSelectedItem().status, "In Active")){
            Alert alt2 = new Alert(Alert.AlertType.ERROR, "Selected Category is Already Deleted item. If you want to Update, Please Select Another Category or ReActivate the Category!!!", ButtonType.OK);
            alt2.showAndWait();
            if (alt2.getResult() == ButtonType.OK) {txtName.requestFocus();}
        } else{
            Alert alt3 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want Update this Category?",ButtonType.YES,ButtonType.NO);
            alt3.showAndWait();
            if (alt3.getResult() == ButtonType.YES){UpdateCategory();}
            if(alt3.getResult() == ButtonType.NO){txtName.requestFocus();}
        }
    }
    private void UpdateCategory(){
        try{
            Statement st = db.con.createStatement();
            int rs = st.executeUpdate("update "+db.schema+".tbl_ctgry set name='"+txtName.getText().toUpperCase().trim()+"',remarks='"+txtRemarks.getText()+"'," +
                    " updated_by='"+LoginController.user+"', updated_on= '"+dateFormat.format(date)+"' where code = '"+txtID.getText()+"'");
            if(rs == 1){
                Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Category Updated Successfully",ButtonType.OK);
                alt.showAndWait();
                if (alt.getResult() ==  ButtonType.OK){clear();}
            }
        } catch (Exception e) {
            Alert alt = new Alert(Alert.AlertType.CONFIRMATION,e.getMessage(),ButtonType.OK);
            alt.showAndWait();
            throw new RuntimeException(e);
        }
    }
    private void DeleteValidation(){
        if (tblView.getSelectionModel().isEmpty()){
            Alert alt = new Alert(Alert.AlertType.ERROR,"Please Select the Category for Delete",ButtonType.OK);
            alt.showAndWait();
            if(alt.getResult() == ButtonType.OK){tblView.requestFocus();}
        } else if (tblView.getSelectionModel().getSelectedItem().status == "In Active") {
            Alert alt1 = new Alert(Alert.AlertType.ERROR, "Selected Category is Already Deleted. Please Select Active Category ", ButtonType.OK);
            alt1.showAndWait();
            if (alt1.getResult() == ButtonType.OK) {tblView.requestFocus();}
        } else{
            Alert alt2 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure, You want to delete this Category?",ButtonType.OK);
            alt2.showAndWait();
            if (alt2.getResult()==ButtonType.OK){DeleteCategory();}
        }
    }
    private void DeleteCategory(){
        try{
            Statement st = db.con.createStatement();
            int rs = st.executeUpdate("update "+db.schema+".tbl_ctgry set status = 0, deleted_by = '"+LoginController.user+"'," +
                    " deleted_on = '"+dateFormat.format(date)+"'  where code ='"+txtID.getText()+"'");
            if(rs == 1){
                Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Category Deleted Successfully",ButtonType.OK);
                alt.showAndWait();
                clear();
            }
        } catch (Exception e) {
            Alert alt = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alt.showAndWait();
            throw new RuntimeException(e);
        }
    }
    private void ActiveCategory(){
        Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure to Re Activate the Category ?",ButtonType.YES,ButtonType.NO);
        alt.showAndWait();
        if (alt.getResult()== ButtonType.YES) {
            try {
                Statement st = db.con.createStatement();
                int rs = st.executeUpdate("update " + db.schema + ".tbl_ctgry set status= 1 where code ='" + txtID.getText() + "'");
                if (rs == 1) {
                    Alert alt1 = new Alert(Alert.AlertType.CONFIRMATION, "Category Activated Successfully", ButtonType.OK);
                    alt1.showAndWait();
                    if (alt1.getResult() == ButtonType.OK) {chkActive.setSelected(true);clear();}
                }
            } catch (Exception e) {
                Alert alt2 = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alt2.showAndWait();
                txtName.requestFocus();
                throw new RuntimeException(e);
            }
        }else{clear();}
    }
    private void CreatePDF(File fileTemp) throws IOException {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(fileTemp));  //INITIALIZE PDF WRITER
            PdfDocument pdf = new PdfDocument(writer);    //Initialize PDF
            Document doc = new Document(pdf, PageSize.A4,false); //Initialize Document and set page size
            doc.setMargins(20,20,20,20); //set margins
            /////////////////////////////Create Fonts ///////////////////////////
            PdfFont head = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont head2 = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont content = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont footerFont = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);
            ////////////// Setting the Titles and subtitles for the pdf ////////////
            Text header = new Text("Global Infotech").setFontSize(24).setFont(head);
            Paragraph para_head = new Paragraph();
            para_head.add(header).setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(.7f);

            Text sub_head = new Text("Category List").setFontSize(15).setFont(head2);
            Paragraph para_subhead = new Paragraph();
            para_subhead.add(sub_head).setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(.7f);

            Text  from_date = new Text("Dated On : "+simpleDate.format(date)).setFontSize(11).setFont(head2);
            Paragraph para_fromdate = new Paragraph();
            para_fromdate.add(from_date).setTextAlignment(TextAlignment.CENTER);
            ///////////////////// ADD Table ////////////////////////
            Table table = new Table(new float[]{10,30,15,10});
            table.setWidth(UnitValue.createPercentValue(100));
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Cell cell = new Cell().add(new Paragraph("Brand ID")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            cell = new Cell().add(new Paragraph("Brand Name")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            cell = new Cell().add(new Paragraph("Remarks")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            cell = new Cell().add(new Paragraph("Status")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            int row = tblData.size();
            for (int i = 0; i < row; i++) {
                objCat sm = tblData.get(i);

                cell = new Cell().add(new Paragraph(sm.getCode())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){
                    cell.setBackgroundColor(ColorConstants.RED);
                }
                table.addCell(cell);

                cell = new Cell().add(new Paragraph(sm.getName())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){
                    cell.setBackgroundColor(ColorConstants.RED);
                }
                table.addCell(cell);

                cell = new Cell().add(new Paragraph(sm.getRemarks())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){
                    cell.setBackgroundColor(ColorConstants.RED);
                }
                table.addCell(cell);

                cell = new Cell().add(new Paragraph(sm.getStatus())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){
                    cell.setBackgroundColor(ColorConstants.RED);
                }
                table.addCell(cell);


            }
            ////////////ADDING CONTENTS TO PDF DOCUMENTS////////////////////////
            doc.add(para_head);
            doc.add(para_subhead);
            doc.add(para_fromdate);
            doc.add(table);
            int n = pdf.getNumberOfPages();
            System.out.println("no of pages = "+n);
            Paragraph footer,printed;
            for (int page = 1; page <= n; page++) {
                footer = new Paragraph(String.format("Page %s of %s", page, n)).setFont(footerFont).setFontSize(6);
                doc.showTextAligned(footer, 550.5f, 20, page,
                        TextAlignment.RIGHT, VerticalAlignment.MIDDLE, 0);

                printed = new Paragraph(String.format("Printed on : "+dateFormat.format(date), page, n)).setFont(footerFont).setFontSize(6);
                doc.showTextAligned(printed, 297.5f, 20, page,
                        TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
            }
            doc.close();
            Desktop.getDesktop().open(fileTemp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void setFadeOutTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), stage_Catgry.getScene().getRoot());
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage_Catgry.close();
            }
        });
    }
    public void btnSave_OnAction(ActionEvent actionEvent) {SaveValidation();}
    public void btnUpdate_onAction(ActionEvent actionEvent) {UpdateValidation();}
    public void btnClear_onAction(ActionEvent actionEvent) {clear();}
    public void btnDelete_onAction(ActionEvent actionEvent) {DeleteValidation();}
    public void btnActive_onAction(ActionEvent actionEvent){ActiveCategory();}
    public void btnPrint_onAction(ActionEvent actionEvent){
        try {
            File file_temp = File.createTempFile("Category List", ".pdf");
            CreatePDF(file_temp);
        } catch (IOException ex) {
            Alert alt = new Alert(Alert.AlertType.ERROR,ex.getMessage(),ButtonType.OK) ;
            alt.show();
            ex.printStackTrace();

        }
    }
    public void btnClose_onAction(ActionEvent actionEvent) { setFadeOutTransition();}
    public void setStage(Stage stageCtgry) {
        this.stage_Catgry = stageCtgry;
        stage_Catgry.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                t.consume();
                setFadeOutTransition();
            }
        });
        stage_Catgry.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    t.consume();
                    setFadeOutTransition();
                }
            }
        });
        stage_Catgry.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.F8) {
                    t.consume();
                    btnClear.fire();
                }
            }
        });
        stage_Catgry.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.F10) {
                    t.consume();
                    btnSave.fire();
                }
            }
        });
        stage_Catgry.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.F11) {
                    t.consume();
                    btnUpdate.fire();
                }
            }
        });
        stage_Catgry.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.F12) {
                    t.consume();
                    btnDelete.fire();
                }
            }
        });
    }
}
