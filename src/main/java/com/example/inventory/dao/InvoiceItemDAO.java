package com.example.inventory.dao;

import com.example.inventory.Database;
import com.example.inventory.model.InvoiceItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAO {
    static Connection con;
    static {
        con = Database.getConnection();
    }
    public static List<InvoiceItems> getInvoiceItemsById(int invoiceId){
        List<InvoiceItems> invoiceItems = new ArrayList<InvoiceItems>();
        String query = "SELECT * from invoice_items where invoice_id = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, invoiceId);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()){
                InvoiceItems invoiceItem = new InvoiceItems();
                invoiceItem.setItemId(resultSet.getInt(3));
                invoiceItem.setItemName(resultSet.getString(4));
                invoiceItem.setQuantity(resultSet.getInt(5));
                invoiceItem.setPrice(resultSet.getDouble(6));
                invoiceItem.setTax(resultSet.getInt(7));
                invoiceItem.setTotalWithoutTax(resultSet.getDouble(8));
                invoiceItem.setTotalWithoutTax(resultSet.getDouble(9));
                invoiceItems.add(invoiceItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return invoiceItems;
    }
}
