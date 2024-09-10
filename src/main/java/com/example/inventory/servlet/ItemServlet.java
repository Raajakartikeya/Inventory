package com.example.inventory.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.inventory.dao.CustomerDAO;
import com.example.inventory.dao.ItemDAO;
import com.example.inventory.model.Item;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet{
	String json;
	static Gson gson = new Gson();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		StringBuilder data = new StringBuilder();
		BufferedReader reader = req.getReader();
		Item item = new Item();
		String line;
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }
		JsonObject jsonObject = gson.fromJson(data.toString(), JsonObject.class);
		if(jsonObject.has("itemName")) {
			item.setItemName(jsonObject.get("itemName").getAsString());
		}
		if(jsonObject.has("itemDescription")) {
			item.setItemDescription(jsonObject.get("itemDescription").getAsString());
		}
		if(jsonObject.has("tax")) {
			item.setTax(jsonObject.get("tax").getAsInt());
		}
		Double itemPrice = 0.0;
		if(jsonObject.has("itemPrice")) {
			itemPrice = jsonObject.get("itemPrice").getAsDouble();
		}
		int rowsAffected = ItemDAO.addItem(item, itemPrice);
		res.setContentType("application/json");
		jsonObject = new JsonObject();
		if(rowsAffected > 0) {
			res.setStatus(HttpServletResponse.SC_CREATED);
    		jsonObject.addProperty("message", "Item added Successfully");
    	}
    	else {
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		jsonObject.addProperty("error", "Item Not Added");
    	}
		json = gson.toJson(jsonObject);
    	res.getWriter().write(json);
    }
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			List<Item> items = ItemDAO.getAllItems();
			json = gson.toJson(items);
		}
		else{
			String[] pathParts = pathInfo.split("/");
			if(pathParts.length == 2) {
				Item item = ItemDAO.getItemById(Integer.parseInt(pathParts[1]));
				json = gson.toJson(item);
			}
			else {
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				json = "Invalid Request";
			}
		}
        res.setContentType("application/json");
        res.getWriter().write(json);   
	}
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			json = "error : Invalid Request, Item ID needed to delete the item";
		}
		else {
			String[] pathParts = pathInfo.split("/");

			if(pathParts.length == 2) {
				int rows = ItemDAO.deleteItemById(Integer.parseInt(pathParts[1]));
				if(rows > 0){
					res.setStatus(HttpServletResponse.SC_OK);
					json = "message : Item deleted successfully";
				}
				else {
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					json = "error : Item Not Deleted";
				}
			}
			else {
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				json = "Invalid Request";
			}
		}
		res.setContentType("application/json");
		res.getWriter().write(json);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws  IOException {
		int rowsAffected;
		JsonObject jsonRes = new JsonObject();
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			json = "Need ID in the URL to Update";
		}
		else{
			String[] pathParts = pathInfo.split("/");
			JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);

			if(pathParts.length == 2) {
				rowsAffected = ItemDAO.updateItem(Integer.parseInt(pathParts[1]), jsonObject);
				if(rowsAffected > 0) {
					jsonRes.addProperty("message", "Item Updated Successfully");
					json = gson.toJson(jsonRes);
				}
				else {
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					json = "Error While Updating Item ";
				}

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
