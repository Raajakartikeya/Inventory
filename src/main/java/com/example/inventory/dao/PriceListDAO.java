package com.example.inventory.dao;

import java.sql.*;
import java.util.List;

import com.example.inventory.Database;
import com.example.inventory.model.PriceList;
import com.example.inventory.model.PriceListName;

public class PriceListDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static void addItemsToPriceList(int priceListId, int itemId, Double sellingPrice) {
		String query = "INSERT INTO price_list VALUES(?, ?, ?)";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, priceListId);
			pst.setInt(2, itemId);
			pst.setDouble(3, sellingPrice);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Adding Price List Items");
		}
	}
	public static int deleteItemFromPriceList(int id){
		int rows = 0;
		String query = "DELETE FROM price_list where item_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Deleting Price List Item");
			return 0;
		}
		return rows;
	}
	public static int updateItemPrice(int priceListId, int itemId, Double sellingPrice){
		int rows = 0;
		String query = "UPDATE PRICE_LIST SET selling_price = ? where item_id = ? and price_list_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setDouble(1, sellingPrice);
			pst.setInt(2, itemId);
			pst.setInt(3, priceListId);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
            System.out.println("Error While Updating Price List Price");
        }
        return rows;
	}
	public static int addNewPriceList(PriceListName priceList){
		int rows = 0;
		String query = "INSERT INTO PRICE_LIST_NAME VALUES (NULL, ?)";
		try {
			con.setAutoCommit(false);
			PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, priceList.getPriceListName());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if(rs.next()){
				int priceListId = rs.getInt(1);
				String query1 = "INSERT INTO PRICE_LIST VALUES (?, ?, ?)";
				PreparedStatement pst1 = con.prepareStatement(query1);
				for(PriceList priceListItems : priceList.getPriceLists()){
					pst1.setInt(1, priceListId);
					pst1.setInt(2, priceListItems.getItemId());
					pst1.setDouble(3, priceListItems.getItemPrice());
					pst1.addBatch();
				}
				pst1.executeBatch();
				con.commit();
				con.setAutoCommit(true);
				return 1;
			}
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rows;
	}

}
