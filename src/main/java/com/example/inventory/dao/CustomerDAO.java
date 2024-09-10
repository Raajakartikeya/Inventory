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
import com.example.inventory.model.Customer;

public class CustomerDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static ArrayList<Customer> getAllCustomers(){
		ArrayList<Customer> customers = new ArrayList<Customer>();
		String query = "select * from Customers";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt(1));
                customer.setCustomerName(rs.getString(2));
                customer.setMobileNo(rs.getString(3));
                customer.setAddress(rs.getString(4));
                customer.setEmailId(rs.getString(5));
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		return customers;
	}
	public static Customer getCustomerById(int id) {
		Customer customer = new Customer();
		String query = "select * from Customers where customer_id = ?";
		try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                customer.setCustomerId(rs.getInt(1));
                customer.setCustomerName(rs.getString(2));
                customer.setMobileNo(rs.getString(3));
                customer.setAddress(rs.getString(4));
                customer.setEmailId(rs.getString(5));
             
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		return customer;
	}
	public static int addCustomer(Customer customer) {
    	String query = "INSERT INTO customers (customer_name, mobile_no, address, email) VALUES (?, ?, ?, ?)";
    	int rows = 0;
    	try {
			PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, customer.getCustomerName());
			pst.setString(2, customer.getMobileNo());
			pst.setString(3, customer.getAddress());
			pst.setString(4, customer.getEmailId());
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Adding Customer");
		}
    	return rows;
    }
	public static int updateCustomer(LinkedHashMap<String, String> mpp, int customer_id) {
		int rows = 0;
		StringBuilder queryBuilder = new StringBuilder("UPDATE CUSTOMERS SET ");
		int i = 1, size = mpp.size();
		
		for(Map.Entry<String, String> mapElement : mpp.entrySet()) {
			String key = mapElement.getKey();
			queryBuilder.append(key + " = ?");
			if(i != size) {
				queryBuilder.append(",");
			}
			i++;
		}
		queryBuilder.append(" where customer_id = ?");
		String query = queryBuilder.toString();
		try {
			PreparedStatement pst = con.prepareStatement(query);
			i = 1;
			for(Map.Entry<String, String> mapElement : mpp.entrySet()) {
				String value = mapElement.getValue();
				pst.setString(i, value);
				i++;
			}
			pst.setInt(i, customer_id);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error While Updating customer");
		}	
		return rows;
	}
	public static int deleteCustomerById(int id){
		int rows = 0;
		String query = "DELETE from CUSTOMERS where customer_id = ?";
		try{
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
            System.out.println("Error while deleting customer");
			return 0;
        }
		return rows;
    }
}