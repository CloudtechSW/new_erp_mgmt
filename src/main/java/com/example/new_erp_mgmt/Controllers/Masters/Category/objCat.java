package com.example.new_erp_mgmt.Controllers.Masters.Category;

public class objCat {
    String code;
    String name;
    String remarks;
    String status;

    public objCat(String code, String name, String remarks, String status) {
        this.code = code;
        this.name = name;
        this.remarks = remarks;
        this.status = status;
    }
    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getRemarks() {return remarks;}
    public void setRemarks(String remarks) {this.remarks = remarks;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}