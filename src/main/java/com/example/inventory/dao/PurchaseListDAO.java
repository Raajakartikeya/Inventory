package com.example.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.inventory.Database;
import com.example.inventory.model.PurchaseList;

public class PurchaseListDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static List<PurchaseList> getPurchaseListByPurchaseId(int id) {
		List<PurchaseList> purchaseLists = new ArrayList<PurchaseList>();
		String query = "SELECT * from PURCHASE_LIST where purchase_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs =  pst.executeQuery();
			while(rs.next()) {
				PurchaseList purchaseList = new PurchaseList();
				purchaseList.setPurchaseListId(rs.getInt(1));
				purchaseList.setItemId(rs.getInt(3));
				purchaseList.setPurchasePrice(rs.getInt(4));
				purchaseList.setPurchaseQuantity(rs.getInt(5));
				purchaseLists.add(purchaseList);
			}
		} catch (SQLException e) {
			System.out.println("Error While Getting Purchase List");
		}
		return purchaseLists;
	}
	public static List<PurchaseList> getPurchaseListByItemId(int id){
		List<PurchaseList> purchaseLists = new ArrayList<PurchaseList>();
		String query = "SELECT * from PURCHASE_LIST where item_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs =  pst.executeQuery();
			while(rs.next()) {
				PurchaseList purchaseList = new PurchaseList();
				purchaseList.setPurchaseListId(rs.getInt(1));
				purchaseList.setItemId(rs.getInt(3));
				purchaseList.setPurchasePrice(rs.getInt(4));
				purchaseList.setPurchaseQuantity(rs.getInt(5));
				purchaseLists.add(purchaseList);
			}
		} catch (SQLException e) {
			System.out.println("Error While Getting Purchase List");
		}
		return purchaseLists;
	}
}
