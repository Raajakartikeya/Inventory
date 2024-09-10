package com.example.inventory.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.inventory.dao.InvoiceDAO;
import com.example.inventory.model.Invoice;
import com.example.inventory.model.InvoiceItems;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InvoiceServlet extends HttpServlet{
	JsonObject data = new JsonObject();
	String json;
	Gson gson = new Gson();
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Invoice invoice = new Invoice();
		data = new Gson().fromJson(req.getReader(), JsonObject.class);
		invoice.setCustomerId(data.get("customerId").getAsInt());
		String dateString = data.get("invoiceDate").getAsString();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate;
		try {
			parsedDate = dateFormat.parse(dateString);
			invoice.setInvoiceDate(new java.sql.Date(parsedDate.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dateString = data.get("dueDate").getAsString();
		try {
			parsedDate = dateFormat.parse(dateString);
			invoice.setDueDate(new java.sql.Date(parsedDate.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JsonArray invoiceItemsJson = data.get("invoiceItems").getAsJsonArray();
        List<InvoiceItems> invoiceItems = new ArrayList<>();
        for(JsonElement jsonElement : invoiceItemsJson) {
			JsonObject itemObject = jsonElement.getAsJsonObject();
        	InvoiceItems invoiceItem = new InvoiceItems();
			invoiceItem.setItemId(itemObject.get("itemId").getAsInt());
			invoiceItem.setQuantity(itemObject.get("quantity").getAsInt());
			int priceListId = itemObject.has("priceListId") ? itemObject.get("priceListId").getAsInt() : 1;
			invoiceItem.setPriceListId(priceListId);
			invoiceItems.add(invoiceItem);
        }
        invoice.setInvoiceItems(invoiceItems);
        int rows = InvoiceDAO.addInvoice(invoice);
        JsonObject jsonObject = new JsonObject();
        if(rows > 0) {
        	res.setStatus(HttpServletResponse.SC_CREATED);
    		jsonObject.addProperty("message", "Invoice added Successfully");
    	}
    	else {
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		jsonObject.addProperty("error", "Invoice Not Added");
    	}
    	json = gson.toJson(jsonObject);
    	res.getWriter().write(json);
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws  IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			List<Invoice> invoices = InvoiceDAO.getAllInvoices();
			json = gson.toJson(invoices);
		}
		else{
			String[] pathParts = pathInfo.split("/");
			if(pathParts.length == 2) {
				Invoice invoice = InvoiceDAO.getInvoiceById(Integer.parseInt(pathParts[1]));
				json = gson.toJson(invoice);
			}
			else {
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				json = "Invalid Request";
			}
		}
		res.setContentType("application/json");
		res.getWriter().write(json);
	}
}
