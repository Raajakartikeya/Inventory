package com.example.inventory.model;

public class Customer {
	private int customerId;
    private String customerName;
    private String mobileNo;
    private String address;
    private String emailId;

    public int getCustomerId(){
        return customerId;
    }
    public String getCustomerName(){
        return customerName;
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
    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }
    public void setCustomerName(String customerName){
        this.customerName = customerName;
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
