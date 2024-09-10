package com.example.inventory.model;

public class Vendor {
	private int vendorId;
    private String vendorName;
    private String mobileNo;
    private String address;
    private String emailId;

    public int getVendorId(){
        return vendorId;
    }
    public String getVendorName(){
        return vendorName;
    }
    public String getMobileNo(){
        return mobileNo;
    }
    public String getAddress(){
        return address;
    }
    public String getEmailId(){
        return emailId;
    }
    public void setVendorId(int vendorId){
        this.vendorId = vendorId;
    }
    public void setVendorName(String vendorName){
        this.vendorName = vendorName;
    }
    public void setMobileNo(String mobileNo){
        this.mobileNo = mobileNo;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setEmailId(String emailId){
        this.emailId = emailId;
    }
}
