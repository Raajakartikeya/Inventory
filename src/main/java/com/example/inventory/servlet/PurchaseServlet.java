package com.example.inventory.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.inventory.dao.PurchaseDAO;
import com.example.inventory.dao.PurchaseListDAO;
import com.example.inventory.model.Purchase;
import com.example.inventory.model.PurchaseList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PurchaseServlet extends HttpServlet{
	JsonObject data = new JsonObject();
	Gson gson = new Gson();
	String json;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		Purchase purchase = new Purchase();
		data = gson.fromJson(req.getReader(), JsonObject.class);
		purchase.setVendorId(data.get("vendorId").getAsInt());
		List<PurchaseList> purchaseLists = new ArrayList<PurchaseList>();
		JsonArray purchaseItems = data.get("purchaseItems").getAsJsonArray();
		for(JsonElement ele : purchaseItems) {
			PurchaseList purchaseList = new PurchaseList();
			purchaseList.setItemId(((JsonObject)ele).get("itemId").getAsInt());
			purchaseList.setPurchasePrice(((JsonObject)ele).get("price").getAsInt());
			purchaseList.setPurchaseQuantity(((JsonObject)ele).get("quantity").getAsInt());
			purchaseLists.add(purchaseList);
		}
		
		JsonObject jsonObject = new JsonObject();
		purchase.setPurchaseLists(purchaseLists);
		int rows = PurchaseDAO.addPurchaseItems(purchase);
		if(rows > 0) {
			res.setStatus(HttpServletResponse.SC_CREATED);
    		jsonObject.addProperty("message", "Purchase added Successfully");
    	}
    	else {
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		jsonObject.addProperty("error", "Purchase Not Added");
    	}
    	json = gson.toJson(jsonObject);
    	res.getWriter().write(json);
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			List<Purchase> purchases = PurchaseDAO.getAllPurchase();
			json = gson.toJson(purchases);
		}
		else{
			String[] pathParts = pathInfo.split("/");
			if(pathParts.length == 2) {
				List<PurchaseList> purchases = PurchaseListDAO.getPurchaseListByPurchaseId(Integer.parseInt(pathParts[1]));
				json = gson.toJson(purchases);
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
