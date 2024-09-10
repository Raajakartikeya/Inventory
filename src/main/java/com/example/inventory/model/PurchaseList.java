package com.example.inventory.model;

public class PurchaseList {
	private int purchaseListId;
	private int itemId;
	private int purchasePrice;
	private int purchaseQuantity;
	public int getPurchaseListId() {
		return purchaseListId;
	}
	public int getItemId() {
		return itemId;
	}
	public int getPurchasePrice() {
		return purchasePrice;
	}
	public int getPurchaseQuantity() {
		return purchaseQuantity;
	}
	public void setPurchaseListId(int purchaseListId) {
		this.purchaseListId = purchaseListId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public void setPurchasePrice(int purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public void setPurchaseQuantity(int purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}
}
