package com.example.inventory.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.example.inventory.dao.CustomerDAO;
import com.example.inventory.dao.VendorDAO;
import com.example.inventory.model.Vendor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class VendorServlet extends HttpServlet{
	String json;
	static Gson gson = new Gson();
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws  IOException{
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			ArrayList<Vendor> vendors = VendorDAO.getAllVendors();
			json = gson.toJson(vendors);
		}
		else {
			String[] pathParts = pathInfo.split("/");
			
			if(pathParts.length == 2) {
				Vendor vendor = VendorDAO.getVendorById(Integer.parseInt(pathParts[1]));
				json = gson.toJson(vendor);
			}
			else {
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json = "Invalid Request";
			}
		}
		
		res.setContentType("application/json");
    	res.getWriter().write(json);
    }
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws  IOException{
		JsonObject jsonObject = new JsonObject();
		Vendor vendor = gson.fromJson(req.getReader(), Vendor.class);
		int rowsAffected = VendorDAO.addNewVendor(vendor);
		res.setContentType("application/json");
		if(rowsAffected > 0) {
			res.setStatus(HttpServletResponse.SC_CREATED);
    		jsonObject.addProperty("message", "Vendor added Successfully");
    	}
    	else {
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		jsonObject.addProperty("error", "Vendor Not Added");
    	}
		
		json = gson.toJson(jsonObject);
    	res.getWriter().write(json);
    }
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws  IOException{
		   int rowsAffected;
		   JsonObject jsonRes = new JsonObject();
		   String pathInfo = req.getPathInfo();
		   if(pathInfo == null || pathInfo.equals("/")) {
				json = "Need ID in the URL to Update";
			}
		   else {
			   String[] pathParts = pathInfo.split("/");
			   
			   LinkedHashMap<String, String> mpp = new LinkedHashMap<String, String>();
			   JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
			   if(jsonObject.has("vendorName")) {
				   mpp.put("vendor_name", jsonObject.get("vendorName").getAsString());
			   }
			   if(jsonObject.has("mobileNo")) {
				   mpp.put("mobile_no", jsonObject.get("mobileNo").getAsString());
			   }
			   if(jsonObject.has("address")) {
				   mpp.put("address", jsonObject.get("address").getAsString());
			   }
			   if(jsonObject.has("email")) {
				   mpp.put("email", jsonObject.get("email").getAsString());
			   }
			   if(pathParts.length == 2) {
				   rowsAffected = VendorDAO.updateVendor(mpp, Integer.parseInt(pathParts[1]));
				   if(rowsAffected > 0) {
					   jsonRes.addProperty("message", "Vendor Updated Successfully");
					   json = gson.toJson(jsonRes);
				   }
				   else {
					   res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		               json = "Error While Updating Vendor Data";
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
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			json = "error : Invalid Request, Vendor ID needed to delete the vendor";
		}
		else {
			String[] pathParts = pathInfo.split("/");

			if(pathParts.length == 2) {
				int rows = VendorDAO.deleteVendorById(Integer.parseInt(pathParts[1]));
				if(rows > 0){
					res.setStatus(HttpServletResponse.SC_OK);
					json = "message : Vendor deleted successfully";
				}
				else {
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					json = "error : Vendor Not Deleted";
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
