package com.example.new_erp_mgmt.Controllers.Masters.Tax;

public class objTax {
    String code,value,remarks,status;
    public objTax(String code, String value, String remarks,String status){
        this.code=code;
        this.value=value;
        this.remarks=remarks;
        this.status=status;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

