package com.example.inventory.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.inventory.Database;
import com.example.inventory.model.Invoice;
import com.example.inventory.model.InvoiceItems;
import com.example.inventory.model.Payment;

public class InvoiceDAO {
	static Connection con;
	static {
		con = Database.getConnection();
	}
	public static int addInvoice(Invoice invoice) {
		int rows = 0;
		String query = "INSERT INTO INVOICES (customer_id, invoice_date, due_date) VALUES (?, ?, ?)";
		try {
			con.setAutoCommit(false);
			PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, invoice.getCustomerId());
			pst.setDate(2, invoice.getInvoiceDate());
			pst.setDate(3, invoice.getDueDate());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if(rs.next()) {
				int invoiceId = rs.getInt(1);
				CallableStatement stmt = con.prepareCall("{CALL insert_invoice_item(?, ?, ?, ?)}");
				for(InvoiceItems invoiceItem : invoice.getInvoiceItems()) {
					int stockCount = StockDAO.getStockById(invoiceItem.getItemId());
					if(stockCount < invoiceItem.getQuantity()) {
						return 0;
					}
					StockDAO.updateStock(invoiceItem.getItemId(), invoiceItem.getQuantity());
					System.out.println("Item ID: " + invoiceItem.getItemId() +
							", Quantity: " + invoiceItem.getQuantity() +
							", Price List ID: " + invoiceItem.getPriceListId());
					stmt.setInt(1, invoiceId);
					stmt.setInt(2, invoiceItem.getItemId());
					stmt.setInt(3, invoiceItem.getQuantity());
					stmt.setInt(4, invoiceItem.getPriceListId());
					stmt.addBatch();
				}
				stmt.executeBatch();

				String query3 = "INSERT INTO payment_details (invoice_id) VALUES (?)";
				PreparedStatement pst3 = con.prepareStatement(query3);
				pst3.setInt(1, invoiceId);
				pst3.executeUpdate();

				con.commit();
				con.setAutoCommit(true);
				return 1;
			}

		} catch (SQLException e) {
			System.out.println("Error While Adding Invoice");
		}
		return rows;
	}
	public static List<Invoice> getAllInvoices(){
		List<Invoice> invoices = new ArrayList<Invoice>();
		String query = "SELECT * FROM invoices";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Invoice invoice = new Invoice();
				invoice.setInvoiceId(rs.getInt(1));
				invoice.setCustomerId(rs.getInt(2));
				invoice.setInvoiceDate(rs.getDate(5));
				invoice.setDueDate(rs.getDate(6));
				Payment payment = PaymentDAO.getPaymentDetailsById(invoice.getInvoiceId());
				invoice.setPayment(payment);
				invoices.add(invoice);
			}
			
		} catch (SQLException e) {
			System.out.println("Error While Getting Invoices");
		}
		return invoices;
	}
	public static Invoice getInvoiceById(int invoiceId){
		Invoice invoice = new Invoice();
		String query = "SELECT * from invoices where invoice_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, invoiceId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				invoice.setInvoiceId(rs.getInt(1));
				invoice.setCustomerId(rs.getInt(2));
				invoice.setInvoiceDate(rs.getDate(5));
				invoice.setDueDate(rs.getDate(6));
				Payment payment = PaymentDAO.getPaymentDetailsById(invoice.getInvoiceId());
				invoice.setPayment(payment);
				List<InvoiceItems> invoiceItems = InvoiceItemDAO.getInvoiceItemsById(invoiceId);
				invoice.setInvoiceItems(invoiceItems);
			}

		} catch (SQLException e) {
			System.out.println("Error While Getting Invoices");
		}
		return invoice;
	}
}
