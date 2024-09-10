package com.example.inventory.dao;

import com.example.inventory.Database;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {
    static Connection con;
    static HashMap<String, String> mpp = new HashMap<String, String>();
    static {
        con = Database.getConnection();
        mpp.put("salesByItem","SELECT item_id,SUM(quantity) as quantity_sold\n" +
                "FROM INVOICE_ITEMS\n" +
                "GROUP BY item_id\n" +
                "ORDER BY quantity_sold DESC;");
        mpp.put("receivables", "SELECT i.customer_id, SUM(balance_due) AS Payables\n" +
                "FROM PAYMENT_DETAILS p\n" +
                "JOIN INVOICES i on p.invoice_id = i.invoice_id\n" +
                "GROUP BY i.customer_id ;");
        mpp.put("salesByCustomer","SELECT i.customer_id, COUNT(i.invoice_id) as invoice_count, SUM(total_without_tax) as sales_without_tax, SUM(total_with_tax) as sales_with_tax\n" +
                "FROM INVOICE_ITEMS IT\n" +
                "JOIN INVOICES I ON IT.invoice_id = I.invoice_id\n" +
                "GROUP BY i.customer_id;");
        mpp.put("stockInfo","SELECT s.item_id, item_name, total_quantity FROM STOCKS S\n" +
                "JOIN ITEMS I\n" +
                "ON S.item_id = I.item_id;");
        mpp.put("paymentsReceived","SELECT I.invoice_id as invoice_id, I.customer_id, SUM(payment_made) as payment_made\n" +
                "FROM PAYMENT_DETAILS P\n" +
                "JOIN INVOICES I\n" +
                "ON P.invoice_id = I.invoice_id\n" +
                "WHERE payment_made != 0\n" +
                "GROUP BY invoice_id ;");
        mpp.put("overdueInvoices","SELECT customer_id, i.invoice_id, invoice_date, due_date, payment_status, balance_due\n" +
                "FROM PAYMENT_DETAILS pd\n" +
                "JOIN INVOICES i ON pd.invoice_id = i.invoice_id\n" +
                "WHERE i.due_date < CURDATE() AND pd.payment_status != 'PAID';\n");
        mpp.put("purchasesByItem","SELECT item_id, SUM(purchase_quantity) as total_purchase_quantity, SUM(purchase_quantity * purchase_price) as amount, ( SUM(purchase_quantity * purchase_price) / SUM(purchase_quantity) ) as avg_price\n" +
                "FROM PURCHASE_LIST\n" +
                "GROUP BY item_id ;");
        mpp.put("invoiceDetails","SELECT payment_status as status, invoice_date, due_date, i.invoice_id, customer_id, total, balance_due\n" +
                "FROM INVOICES I\n" +
                "JOIN PAYMENT_DETAILS\n" +
                "ON I.invoice_id = PAYMENT_DETAILS.invoice_id;");
        mpp.put("purchasesByVendor","SELECT COUNT(p.purchase_id) as purchase_count, vendor_id, SUM(purchase_quantity * purchase_price) as amount\n" +
                "FROM PURCHASES P\n" +
                "JOIN PURCHASE_LIST PL\n" +
                "ON P.purchase_id = PL.purchase_id\n" +
                "GROUP BY vendor_id;");
        mpp.put("purchaseDetails","SELECT p.purchase_id, vendor_id, SUM(purchase_quantity * purchase_price) as amount\n" +
                "FROM PURCHASES P\n" +
                "JOIN PURCHASE_LIST PL\n" +
                "ON P.purchase_id = PL.purchase_id\n" +
                "GROUP BY P.purchase_id;");
    }
    public static String getReport(String reportName){
        Gson gson;
        List<Map<String, Object>> rows = new ArrayList<>();
        try {
            String temp = mpp.get(reportName);
            if(temp == null){
                return "";
            }
            PreparedStatement pst = con.prepareStatement(temp);
            ResultSet resultSet = pst.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }

                rows.add(row);
            }

            gson = new GsonBuilder().setPrettyPrinting().create();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gson.toJson(rows);
    }
}
