package com.example.inventory.model;

import java.util.List;

public class Purchase {
	private int purchaseId;
	private int vendorId;
	private List<PurchaseList> purchaseLists;
	
	public int getPurchaseId() {
		return purchaseId;
	}
	public int getVendorId() {
		return vendorId;
	}
	public List<PurchaseList> getPurchaseLists(){
		return purchaseLists;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public void setPurchaseLists(List<PurchaseList> purchaseLists) {
		this.purchaseLists = purchaseLists;
	}
	

}
