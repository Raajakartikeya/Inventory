package com.example.inventory.model;

import java.util.List;

public class PriceListName {
	private int priceListId;
	private String priceListName;
	List<PriceList> priceLists;
	public void setPriceListId(int priceListId) {
		this.priceListId = priceListId;
	}
	public void setPriceListName(String priceListName) {
		this.priceListName = priceListName;
	}
	public int getPriceListId() {
		return priceListId;
	}
	public String getPriceListName() {
		return priceListName;
	}
	public void setPriceList(List<PriceList> priceLists) {
		this.priceLists = priceLists;
	}
	public List<PriceList> getPriceLists(){
		return priceLists;
	}
}
