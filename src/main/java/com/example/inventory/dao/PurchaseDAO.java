package com.example.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.inventory.Database;
import com.example.inventory.model.Purchase;
import com.example.inventory.model.PurchaseList;
import com.google.gson.JsonObject;

public class PurchaseDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static int addPurchaseItems(Purchase purchase) {
		int rows = 0;
		String query1 = "INSERT into PURCHASES VALUES (NULL, ?)";
		try {
			con.setAutoCommit(false);
			PreparedStatement pst1 = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
			pst1.setInt(1, purchase.getVendorId());
			int rowsAffected1 = pst1.executeUpdate();
			if(rowsAffected1 > 0) {
				ResultSet rs = pst1.getGeneratedKeys();
				if(rs.next()) {
					int purchaseId = rs.getInt(1);
					String query2 = "INSERT INTO purchase_list VALUES (NULL, ?, ?, ? , ?)";
					String query3 = "UPDATE stocks SET total_quantity = total_quantity + ? where item_id = ?";
					PreparedStatement pst2 = con.prepareStatement(query2);
					PreparedStatement pst3 = con.prepareStatement(query3);
					for(PurchaseList purchaseList : purchase.getPurchaseLists()) {
						pst2.setInt(1, purchaseId);
						pst2.setInt(2, purchaseList.getItemId());
						pst2.setInt(3, purchaseList.getPurchasePrice());
						pst2.setInt(4, purchaseList.getPurchaseQuantity());
						pst2.addBatch();
						
						pst3.setInt(1, purchaseList.getPurchaseQuantity());
						pst3.setInt(2, purchaseList.getItemId());
						pst3.addBatch();
					}
					pst2.executeBatch();
					pst3.executeBatch();
					con.commit();
					con.setAutoCommit(true);
					return 1;
				}
			}
			else {
				System.out.println("Error While Inserting Purchase");
			}
			
		} catch (SQLException e) {
			System.out.println("Error While adding Purchase Items");
		}
		
		return rows;
	}
	public static List<Purchase> getAllPurchase(){
		List<Purchase> purchases = new ArrayList<Purchase>();
		String query = "SELECT * FROM PURCHASES";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Purchase purchase = new Purchase();
				purchase.setPurchaseId(rs.getInt(1));
				purchase.setVendorId(rs.getInt(2));
				purchase.setPurchaseLists(PurchaseListDAO.getPurchaseListByPurchaseId(purchase.getPurchaseId()));
				purchases.add(purchase);
			}
		} catch (SQLException e) {
			System.out.println("Error While Getting Purchase List");
		}
		return purchases;
	}
}
