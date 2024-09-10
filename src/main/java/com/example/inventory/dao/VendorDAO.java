package com.example.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.inventory.Database;
import com.example.inventory.model.Vendor;

public class VendorDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static ArrayList<Vendor> getAllVendors(){
		ArrayList<Vendor> vendors = new ArrayList<Vendor>();
		String query = "SELECT * from VENDORS";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs =  pst.executeQuery(query);
			while(rs.next()) {
				Vendor vendor = new Vendor();
				vendor.setVendorId(rs.getInt(1));
				vendor.setVendorName(rs.getString(2));
				vendor.setMobileNo(rs.getString(3));
				vendor.setAddress(rs.getString(4));
				vendor.setEmailId(rs.getString(5));
				vendors.add(vendor);
				
			}
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		return vendors;
	}
	public static Vendor getVendorById(int id) {
		Vendor vendor = new Vendor();
		String query = "SELECT * from VENDORS where vendor_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs =  pst.executeQuery();
			while(rs.next()) {
				vendor.setVendorId(rs.getInt(1));
				vendor.setVendorName(rs.getString(2));
				vendor.setMobileNo(rs.getString(3));
				vendor.setAddress(rs.getString(4));
				vendor.setEmailId(rs.getString(5));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
		return vendor;
	}
	public static int addNewVendor(Vendor vendor) {
		String query = "INSERT INTO vendors (vendor_name, mobile_no, address, email) VALUES (?, ?, ?, ?)";
    	int rows = 0;
    	try {
			PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, vendor.getVendorName());
			pst.setString(2, vendor.getMobileNo());
			pst.setString(3, vendor.getAddress());
			pst.setString(4, vendor.getEmailId());
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Adding Customer");
		}
    	return rows;
	}
	public static int updateVendor(LinkedHashMap<String, String> mpp, int vendor_id) {
		int rows = 0;
		StringBuilder queryBuilder = new StringBuilder("UPDATE VENDORS SET ");
		int i = 1, size = mpp.size();
		
		for(Map.Entry<String, String> mapElement : mpp.entrySet()) {
			String key = mapElement.getKey();
			queryBuilder.append(key + " = ?");
			if(i != size) {
				queryBuilder.append(",");
			}
			i++;
		}
		queryBuilder.append(" where vendor_id = ?");
		String query = queryBuilder.toString();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			i = 1;
			for(Map.Entry<String, String> mapElement : mpp.entrySet()) {
				String value = mapElement.getValue();
				pst.setString(i, value);
				i++;
			}
			pst.setInt(i, vendor_id);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Updating Vendor");
		}	
		return rows;
	}
	public static int deleteVendorById(int id){
		int rows = 0;
		String query = "DELETE from VENDORS where vendor_id = ?";
		try{
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error while deleting Vendor ");
			return 0;
		}
		return rows;
	}
}
