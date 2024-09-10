package com.example.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.inventory.Database;
import com.example.inventory.model.Item;
import com.google.gson.JsonObject;

public class ItemDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static int addItem(Item item, Double itemPrice) {
		int rows = 0;
		String query = "INSERT INTO items (item_name, item_description, tax) VALUES (?, ?, ?)";
		if(itemPrice == 0) {
			return 0;
		}
		try {
			PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, item.getItemName());
			pst.setString(2, item.getItemDescription());
			pst.setInt(3, item.getTax());
			rows = pst.executeUpdate();
			
			ResultSet rs = pst.getGeneratedKeys();
			
			if(rs.next()) {
				item.setItemId(rs.getInt(1));
			}
			//Initiating Stock Level to 0
			String query2 = "INSERT into STOCKS VALUES (? , 0)";
			PreparedStatement pst2 = con.prepareStatement(query2);
			pst2.setInt(1, item.getItemId());
			pst2.executeUpdate();
			
			//Adding Item Selling Price to Default Price List
			PriceListDAO.addItemsToPriceList(1, item.getItemId(),itemPrice);
			
		} catch (SQLException e) {
			System.out.println("Error While Adding Items");
		}
		return rows;
	}
	public static List<Item> getAllItems(){
		List<Item> items = new ArrayList<Item>();
		String query = "select * from items";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
               Item item = new Item();
               item.setItemId(rs.getInt(1));
               item.setItemName(rs.getString(2));
               item.setItemDescription(rs.getString(3));
               item.setTax(rs.getInt(4));
               items.add(item);
            }
		} catch (SQLException e) {
			 throw new RuntimeException(e);
		}
		return items;
	}
	public static Item getItemById(int id){
		Item item = new Item();
		String query = "select * from items where item_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				item.setItemId(rs.getInt(1));
				item.setItemName(rs.getString(2));
				item.setItemDescription(rs.getString(3));
				item.setTax(rs.getInt(4));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return item;
	}
	public static int deleteItemById(int id){
		int rows = 0;
		System.out.println(PurchaseListDAO.getPurchaseListByItemId(id).isEmpty());
		if(PurchaseListDAO.getPurchaseListByItemId(id).isEmpty()) {
			if ((StockDAO.deleteStockItem(id) > 0) && (PriceListDAO.deleteItemFromPriceList(id) > 0)) {
				String query = "DELETE from ITEMS where item_id = ?";
				try {
					PreparedStatement pst = con.prepareStatement(query);
					pst.setInt(1, id);
					rows = pst.executeUpdate();
				} catch (SQLException e) {
					System.out.println("Error while deleting item");
					return 0;
				}
			}
		}
		return rows;
	}
	public static int updateItem(int id, JsonObject jsonObject){
		int rows = 0;
		String itemName = jsonObject.has("itemName") ? jsonObject.get("itemName").getAsString() : null;
		String itemDescription = jsonObject.has("itemDescription") ? jsonObject.get("itemDescription").getAsString() : null;
		Integer tax = jsonObject.has("tax") ? jsonObject.get("tax").getAsInt() : null;
		Double sellingPrice = jsonObject.has("price") ?  jsonObject.get("price").getAsDouble() : null;
		StringBuilder queryBuilder = new StringBuilder("UPDATE ITEMS SET ");
		boolean first = true;
		if(itemName != null){
			queryBuilder.append("item_name = ?");
			first = false;
		}
		if(itemDescription != null){
			if(!first) queryBuilder.append(",");
			queryBuilder.append("item_description = ?");
			first = false;
		}
		if(tax != null){
			if(!first) queryBuilder.append(",");
			queryBuilder.append("tax = ?");
			first = false;
		}
		queryBuilder.append("where item_id = ?");
		String query = queryBuilder.toString();
		if(!first){
			try {
				int i = 1;
				PreparedStatement pst = con.prepareStatement(query);
				if(itemName != null){
					pst.setString(i++, itemName);
				}
				if(itemDescription != null){
					pst.setString(i++, itemDescription);
				}
				if(tax != null){
					pst.setInt(i++, tax);
				}
				pst.setInt(i, id);
				rows = pst.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error While updating Item ID");
				return 0;
			}
		}
		if(sellingPrice != null){
			rows = PriceListDAO.updateItemPrice(1, id, sellingPrice);
		}

		return rows;
	}
}
