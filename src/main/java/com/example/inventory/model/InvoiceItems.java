package com.example.inventory.model;

public class InvoiceItems {
	private int itemId;
	private int quantity;
	private String itemName;
	private Double price;
	private int tax;
	private Double totalWithoutTax;
	private Double totalWithTax;
	private Integer priceListId;
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getQuantity() {
		return quantity;
	}
	public int getItemId() {
		return itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	public Double getTotalWithoutTax() {
		return totalWithoutTax;
	}
	public void setTotalWithoutTax(Double totalWithoutTax) {
		this.totalWithoutTax = totalWithoutTax;
	}
	public Double getTotalWithTax() {
		return totalWithTax;
	}
	public void setTotalWithTax(Double totalWithTax) {
		this.totalWithTax = totalWithTax;
	}
	public void setPriceListId(Integer priceListId) {
		this.priceListId = priceListId;
	}
	public Integer getPriceListId() {
		return priceListId;
	}
}
