package com.example.new_erp_mgmt.Database;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMysql {
    public Connection con = null;
    public String schema = "erp_mgmt";
    public String username = "root";
    public String password = "Cloudtech@8080";
    public String url = "jdbc:mysql://localhost:3306/"+schema+"?allowPublicKeyRetrieval=true&useSSL=false";

    public DBMysql() {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch ( SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.show();

        }
    }
}

