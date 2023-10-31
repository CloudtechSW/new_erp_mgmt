module com.example.new_erp_mgmt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires barcodes;
    requires bouncy.castle.connector;
    requires commons;
    requires font.asian;
    requires forms;
    requires hyph;
    requires io;
    requires kernel;
    requires layout;
    requires pdfa;
    requires sign;
    requires styled.xml.parser;
    requires svg;
    requires java.desktop;
    requires javafx.graphics;
requires javafx.swing;

    opens com.example.new_erp_mgmt to javafx.fxml;
    exports com.example.new_erp_mgmt;
    exports com.example.new_erp_mgmt.Database;
    exports com.example.new_erp_mgmt.Controllers.User;
    exports com.example.new_erp_mgmt.Controllers.Masters.Brand;
    exports com.example.new_erp_mgmt.Controllers.Masters.Category;
    exports com.example.new_erp_mgmt.Controllers.Masters.Department;
    exports com.example.new_erp_mgmt.Controllers.Masters.Designation;
    exports com.example.new_erp_mgmt.Controllers.Masters.ItemMaster;
    exports com.example.new_erp_mgmt.Controllers.Masters.SupplierMaster;
    exports com.example.new_erp_mgmt.Controllers.Masters.Tax;

}