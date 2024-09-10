package com.example.inventory.model;

import java.sql.Date;
import java.util.List;

public class Invoice {
	private int invoiceId;
	private int customerId;
	private Date invoiceDate;
	private Date dueDate;
	private List<InvoiceItems> invoiceItems;
	private Payment payment;
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public void setInvoiceItems(List<InvoiceItems> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public int getInvoiceId() {
		return invoiceId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public Payment getPaymentDetails() {
		return payment;
	}
	public List<InvoiceItems> getInvoiceItems(){
		return invoiceItems;
	}
	

}
