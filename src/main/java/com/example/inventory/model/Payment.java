package com.example.inventory.model;

public class Payment {
	private int subTotal;
	private int total;
	private int taxAmount;
	private String paymentStatus;
	private int paymentMade;
	private int balanceDue;
	
	public void setSubTotal(int subTotal) {
		this.subTotal = subTotal;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void setTaxAmount(int taxAmount) {
		this.taxAmount = taxAmount;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public void setPaymentMade(int paymentMade) {
		this.paymentMade = paymentMade;
	}
	public void setBalanceDue(int balanceDue) {
		this.balanceDue = balanceDue;
	}
	public int getSubTotal() {
		return subTotal;
	}
	public int getTotal() {
		return total;
	}
	public int getTaxAmount() {
		return taxAmount;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public int getPaymentMade() {
		return paymentMade;
	}
	public int getBalanceDue() {
		return balanceDue;
	}

}
