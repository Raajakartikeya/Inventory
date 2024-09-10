package com.example.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.example.inventory.Database;
import com.example.inventory.model.Payment;

public class PaymentDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static int updatePayment(int id, double paidAmount) {
		int rows = 0;
		String query = "UPDATE PAYMENT_DETAILS SET payment_made = payment_made + ? where invoice_id = ?";
		if(getBalanceDue(id) == -1) {
			return 0;
		}
		else if(paidAmount > getBalanceDue(id)) {
			return 2;
		}
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setDouble(1, paidAmount);
			pst.setInt(2, id);
			rows = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error WHile Updating Payment Made");
		}
		return rows;
	}
	public static int getBalanceDue(int id) {
		int balanceDue = -1;
		String query = "SELECT balance_due from PAYMENT_DETAILS where invoice_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				balanceDue = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Error While Getting Balance Due");
		}
		return balanceDue;
	}
	public static Payment getPaymentDetailsById(int id) {
		Payment payment = new Payment();
		String query = "SELECT * from PAYMENT_DETAILS where invoice_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				payment.setSubTotal(rs.getInt(2));
				payment.setTaxAmount(rs.getInt(3));
				payment.setTotal(rs.getInt(4));
				payment.setPaymentStatus(rs.getString(5));
				payment.setPaymentMade(rs.getInt(6));
				payment.setBalanceDue(rs.getInt(7));
			}
		} catch (SQLException e) {
			System.out.println("Error While Getting Payment Details");
		}
		return payment;
	}
}
