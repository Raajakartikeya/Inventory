package com.example.inventory.model;

public class Item {
	private int itemId;
	private String itemName;
	private String itemDescription;
	private int tax;
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	public int getItemId() {
		return itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public String getItemDescription(){
		return itemDescription;
	}
	public int getTax() {
		return tax;
	}
}
