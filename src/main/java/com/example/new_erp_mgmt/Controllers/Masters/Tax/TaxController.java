package com.example.new_erp_mgmt.Controllers.Masters.Tax;

import com.example.new_erp_mgmt.Controllers.User.LoginController;
import com.example.new_erp_mgmt.Database.DBMysql;
import com.itextpdf.io.exceptions.IOException;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class TaxController implements Initializable {

    public TextField txtID;
    public TextField txtValue;
    public TextField txtRemarks;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnClear;
    public Button btnDelete;
    public Button btnActive;
    public Button btnClose;
    public Button btnPrint;
    public TableView<objTax> tblView;
    public TableColumn colSlNo;
    public TableColumn colID;
    public TableColumn colValue;
    public TableColumn colRemarks;
    public TableColumn colStatus;
    public CheckBox chkActive;
    private Stage stage_Tax;
    DBMysql db = new DBMysql();
    ObservableList<objTax> tblData = FXCollections.observableArrayList();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    DateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clear();
        setCode();
        listener();
        setTable();
        chkActive.setSelected(true);
    }
    private void clear(){
        btnSave.setVisible(true);
        btnClear.setVisible(true);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        btnActive.setVisible(false);
        txtValue.clear();
        txtRemarks.clear();
        populateTable();
        setCode();
        Platform.runLater(()->txtValue.requestFocus());
    }
    private void setCode(){
        String id = "0";
        try{
            Statement st = db.con.createStatement();
            ResultSet rs = st.executeQuery("select count(code), max(code) from "+db.schema+".tbl_tax");
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
                txtID.setText("TAX"+no);
            }
        } catch (Exception ex) {
            Alert alt = new Alert(Alert.AlertType.ERROR,ex.getMessage(),ButtonType.OK);
            alt.show();
            throw new RuntimeException(ex);
        }
    }
    private void setTable(){
        colID.setCellValueFactory(new PropertyValueFactory<objTax,String>("code"));
        colValue.setCellValueFactory(new PropertyValueFactory<objTax,String>("value"));
        colRemarks.setCellValueFactory(new PropertyValueFactory<objTax,String>("remarks"));
        colStatus.setCellValueFactory(new PropertyValueFactory<objTax,String>("status"));
        colSlNo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<objTax, objTax>, ObservableValue<objTax>>() {
            @Override
            public ObservableValue<objTax> call(TableColumn.CellDataFeatures<objTax, objTax> p) {
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });
        colSlNo.setCellFactory(new Callback<TableColumn<objTax, objTax>, TableCell<objTax, objTax>>() {
            @Override public TableCell<objTax, objTax> call(TableColumn<objTax, objTax> param) {
                return new TableCell<objTax, objTax>() {
                    @Override
                    protected void updateItem(objTax item, boolean empty) {
                        super.updateItem(item, empty);
                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex()+1+"");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        colSlNo.setSortable(false);
        tblView.setItems(tblData);
    }
    private void populateTable(){
        tblData.clear();
        try{
            Statement st =db.con.createStatement();
            String sts;
            ResultSet rs;
            if(chkActive.isSelected()){rs  = st.executeQuery("select * from " + db.schema + ".tbl_tax where status = 1");}
            else{rs = st.executeQuery("select * from " + db.schema + ".tbl_tax ");}
            while(rs.next()){
                if(rs.getString("status").equalsIgnoreCase("1")){sts="Active";}else{sts="In Active";}
                tblData.add(new objTax(rs.getString("code"),rs.getString("tax"),rs.getString("remarks"),sts));
            }
        } catch (Exception e) {
            Alert alt = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alt.showAndWait();
            throw new RuntimeException(e);
        }
    }
    private void listener(){
        txtValue.addEventFilter(KeyEvent.KEY_RELEASED,new EventHandler<KeyEvent>(){
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
                    fetchData(tblView.getSelectionModel().getSelectedItem());
                }
            }
        });
    }
    private void fetchData(objTax obj){
        btnSave.setVisible(false);
        btnClear.setVisible(false);
        btnUpdate.setVisible(true);
        if(obj.getStatus() == "In Active"){btnDelete.setVisible(false); btnActive.setVisible(true);}
        else{btnActive.setVisible(false); btnDelete.setVisible(true);}
        txtID.setText(obj.getCode());
        txtValue.setText(obj.getValue());
        txtRemarks.setText(obj.getRemarks());
    }
    private void saveValidation(){
        if ( txtID.getText().equalsIgnoreCase("") || txtValue.getText().equalsIgnoreCase("")){
            Alert alt = new Alert(Alert.AlertType.ERROR,"Please Enter GST Value",ButtonType.OK);
            alt.showAndWait();
            if(alt.getResult()==ButtonType.OK){txtValue.requestFocus();}
        }else {
            Alert alt1 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure, You want to save this TAX ?",ButtonType.YES,ButtonType.NO);
            alt1.showAndWait();
            if (alt1.getResult() == ButtonType.YES){saveData();}else{txtValue.requestFocus();}
        }
    }
    private void saveData(){
        try{
            Statement st = db.con.createStatement();
            int rs = st.executeUpdate("insert into "+db.schema+".tbl_tax values(null,'"+txtID.getText()+"',"+txtValue.getText()+",'"+txtRemarks.getText()+"',1," +
                    " '"+ LoginController.user +"','"+dateFormat.format(date)+"',null,null,null,null)");
            if(rs == 1){
                Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Tax Value Saved Successfully!",ButtonType.OK);
                alt.showAndWait();
                if(alt.getResult()==ButtonType.OK){clear();}
            }
        } catch (Exception ex) {
            Alert alt1 = new Alert(Alert.AlertType.ERROR,ex.getMessage(),ButtonType.OK);
            alt1.showAndWait();
            txtValue.requestFocus();
            throw new RuntimeException(ex);
        }
    }
    private void updateValidation(){
        if(tblView.getSelectionModel().isEmpty()){
            Alert alt1 = new Alert(Alert.AlertType.ERROR ,"Please Select a Tax Value from Table",ButtonType.OK);
            alt1.showAndWait();
            if (alt1.getResult() == ButtonType.OK){tblView.requestFocus();}
        }else if(txtID.getText().equalsIgnoreCase("") || txtValue.getText().equalsIgnoreCase("") ) {
            Alert alt2 = new Alert(Alert.AlertType.ERROR, "Please Enter a Tax Value!!", ButtonType.OK);
            alt2.showAndWait();
            if (alt2.getResult() == ButtonType.OK) {txtValue.requestFocus();}
        }else if(Objects.equals(tblView.getSelectionModel().getSelectedItem().status, "In Active")){
            Alert alt2 = new Alert(Alert.AlertType.ERROR, "Selected Tax Value is Already Deleted item. If you want to Update, Please Select Another Tax or ReActivate the Tax Value!!!", ButtonType.OK);
            alt2.showAndWait();
            if (alt2.getResult() == ButtonType.OK) {txtValue.requestFocus();}
        } else{
            Alert alt3 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want Update this Tax Value?",ButtonType.YES,ButtonType.NO);
            alt3.showAndWait();
            if (alt3.getResult() == ButtonType.YES){updateData();}
            if(alt3.getResult() == ButtonType.NO){txtValue.requestFocus();}
        }
    }
    private void updateData(){
        try{
            Statement st = db.con.createStatement();
            int rs = st.executeUpdate("update "+db.schema+".tbl_tax set tax="+txtValue.getText()+",remarks='"+txtRemarks.getText()+"'," +
                    " updated_by='"+LoginController.user+"', updated_on='"+dateFormat.format(date)+"' where code = '"+txtID.getText()+"'");
            if(rs == 1){
                Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Tax Updated Successfully",ButtonType.OK);
                alt.showAndWait();
                if (alt.getResult() ==  ButtonType.OK){clear();}
            }
        } catch (Exception e) {
            Alert alt = new Alert(Alert.AlertType.CONFIRMATION,e.getMessage(),ButtonType.OK);
            alt.showAndWait();
            txtValue.requestFocus();
            throw new RuntimeException(e);
        }
    }
    private void deleteValidation(){
        if (tblView.getSelectionModel().isEmpty()){
            Alert alt = new Alert(Alert.AlertType.ERROR,"Please Select the Tax Value for Delete",ButtonType.OK);
            alt.showAndWait();
            if(alt.getResult() == ButtonType.OK){tblView.requestFocus();}
        } else if (tblView.getSelectionModel().getSelectedItem().status == "In Active") {
            Alert alt1 = new Alert(Alert.AlertType.ERROR, "Selected Tax is Already Deleted. Please Select Active Tax ", ButtonType.OK);
            alt1.showAndWait();
            if (alt1.getResult() == ButtonType.OK) {tblView.requestFocus();}
        } else{
            Alert alt2 = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure, You want to delete this Tax Value?",ButtonType.OK);
            alt2.showAndWait();
            if (alt2.getResult()==ButtonType.OK){deleteData();}
        }
    }
    private void deleteData(){
        try{
            Statement st = db.con.createStatement();
            int rs = st.executeUpdate("update "+db.schema+".tbl_tax set status = 0," +
                    " deleted_by = '"+LoginController.user+"',deleted_on = '"+dateFormat.format(date)+"' where code ='"+txtID.getText()+"'");
            if(rs == 1){
                Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Tax Deleted Successfully",ButtonType.OK);
                alt.showAndWait();
                clear();
            }
        } catch (Exception e) {
            Alert alt = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alt.showAndWait();
            txtValue.requestFocus();
            throw new RuntimeException(e);
        }
    }
    private void activeData(){
        Alert alt = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure to Re Activate the Tax Value ?",ButtonType.YES,ButtonType.NO);
        alt.showAndWait();
        if (alt.getResult()== ButtonType.YES) {
            try {
                Statement st = db.con.createStatement();
                int rs = st.executeUpdate("update " + db.schema + ".tbl_tax set status= 1 where code ='" + txtID.getText() + "'");
                if (rs == 1) {
                    Alert alt1 = new Alert(Alert.AlertType.CONFIRMATION, "Tax Activated Successfully", ButtonType.OK);
                    alt1.showAndWait();
                    if (alt1.getResult() == ButtonType.OK) {chkActive.setSelected(true);clear();}
                }
            } catch (Exception e) {
                Alert alt2 = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alt2.showAndWait();
                txtValue.requestFocus();
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

            Text sub_head = new Text("Tax List").setFontSize(15).setFont(head2);
            Paragraph para_subhead = new Paragraph();
            para_subhead.add(sub_head).setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(.7f);

            Text  from_date = new Text("Dated On : "+simpleDate.format(date)).setFontSize(11).setFont(head2);
            Paragraph para_fromdate = new Paragraph();
            para_fromdate.add(from_date).setTextAlignment(TextAlignment.CENTER);
            ///////////////////// ADD Table ////////////////////////
            Table table = new Table(new float[]{10,30,15,10});
            table.setWidth(UnitValue.createPercentValue(100));
            table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

            Cell cell = new Cell().add(new Paragraph("Tax ID")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            cell = new Cell().add(new Paragraph("Tax Value")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            cell = new Cell().add(new Paragraph("Remarks")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            cell = new Cell().add(new Paragraph("Status")).setTextAlignment(TextAlignment.CENTER).setFont(head2);
            table.addHeaderCell(cell);

            int row = tblData.size();
            for (int i = 0; i < row; i++) {
                objTax sm = tblData.get(i);

                cell = new Cell().add(new Paragraph(sm.getCode())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){cell.setBackgroundColor(ColorConstants.RED);}
                table.addCell(cell);

                cell = new Cell().add(new Paragraph(sm.getValue())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){cell.setBackgroundColor(ColorConstants.RED);}
                table.addCell(cell);

                cell = new Cell().add(new Paragraph(sm.getRemarks())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){cell.setBackgroundColor(ColorConstants.RED);}
                table.addCell(cell);

                cell = new Cell().add(new Paragraph(sm.getStatus())).setTextAlignment(TextAlignment.CENTER).setFont(content).setFontSize(8);
                if(sm.getStatus().equalsIgnoreCase("In Active")){cell.setBackgroundColor(ColorConstants.RED);}
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void setFadeOutTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), stage_Tax.getScene().getRoot());
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage_Tax.close();
            }
        });
    }
    public void btnSave_onAction(ActionEvent actionEvent) {saveValidation();}
    public void btnUpdate_onAction(ActionEvent actionEvent){updateValidation();}
    public void btnDelete_onAction(ActionEvent actionEvent){deleteValidation();}
    public void btnClear_onAction(ActionEvent actionEvent){clear();}
    public void btnActive_onAction(ActionEvent actionEvent){activeData();}
    public void btnPrint_onAction(ActionEvent actionEvent){
        try {
            File file_temp = File.createTempFile("Tax List", ".pdf");
            CreatePDF(file_temp);
        } catch (IOException | java.io.IOException ex) {
            Alert alt = new Alert(Alert.AlertType.ERROR,ex.getMessage(),ButtonType.OK) ;
            alt.show();
            ex.printStackTrace();
        }
    }
    public void btnClose_onAction(ActionEvent actionEvent){setFadeOutTransition();}
    public void setStage(Stage stageTax) {this.stage_Tax=stageTax;
        stage_Tax.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                t.consume();
                setFadeOutTransition();
            }
        });
        stage_Tax.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    t.consume();
                    setFadeOutTransition();
                }
            }
        });
        stage_Tax.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.F8) {
                    t.consume();
                    btnClear.fire();
                }
            }
        });
        stage_Tax.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.F10) {
                    t.consume();
                    btnSave.fire();
                }
            }
        });
        stage_Tax.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.F11) {
                    t.consume();
                    btnUpdate.fire();
                }
            }
        });
        stage_Tax.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
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
