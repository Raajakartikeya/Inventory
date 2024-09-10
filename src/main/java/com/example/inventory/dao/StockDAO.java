package com.example.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.inventory.Database;
import com.example.inventory.model.Item;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class StockDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static int getStockById(int id) {
		int result = 0;
		String query = "SELECT total_quantity from STOCKS where item_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			System.out.println("Error While Getting Stock Count");
		}
		return result;
	}
	public static void updateStock(int id, int quantity) {
		String query = "UPDATE STOCKS SET total_quantity = total_quantity - ? where item_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, quantity);
			pst.setInt(2, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Updating Stock Count");
		}
	}
	public static int deleteStockItem(int id){
		int rows = 0;
		String query = "DELETE FROM stocks where item_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Deleting Stock Item");
		}
		return rows;
	}
	public static JsonArray getItemStocks(){
		JsonArray jsonArray = new JsonArray();
		String query = "SELECT * from STOCKS";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet resultSet =  pst.executeQuery();
			while(resultSet.next()){
				Item item = ItemDAO.getItemById(resultSet.getInt(1));
				int stockQuantity = resultSet.getInt(2);
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("itemId", item.getItemId());
				jsonObject.addProperty("itemName", item.getItemDescription());
				jsonObject.addProperty("Stock Quantity", stockQuantity);
				jsonArray.add(jsonObject);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return jsonArray;
	}
}
