package com.example.inventory.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.example.inventory.dao.CustomerDAO;
import com.example.inventory.model.Customer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomerServlet extends HttpServlet{
	String json;
	static Gson gson = new Gson();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			ArrayList<Customer> customers = CustomerDAO.getAllCustomers();
			json = new Gson().toJson(customers);
		}
		else {
			String[] pathParts = pathInfo.split("/");
			
			if(pathParts.length == 2) {
				Customer customer = CustomerDAO.getCustomerById(Integer.parseInt(pathParts[1]));
				json = gson.toJson(customer);
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
	    String json;
    	Customer customer = gson.fromJson(req.getReader(), Customer.class);
    	int rowsAffected = CustomerDAO.addCustomer(customer);
    	res.setContentType("application/json");
    	if(rowsAffected > 0) {
    		res.setStatus(HttpServletResponse.SC_CREATED);
    		jsonObject.addProperty("message", "Customer added Successfully");
    	}
    	else {
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		jsonObject.addProperty("error", "Customer Not Added");
    	}
    	json = gson.toJson(jsonObject);
    	res.getWriter().write(json); 	
   }
   protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException{
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
		   if(jsonObject.has("customerName")) {
			   mpp.put("customer_name", jsonObject.get("customerName").getAsString());
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
			   rowsAffected = CustomerDAO.updateCustomer(mpp, Integer.parseInt(pathParts[1]));
			   if(rowsAffected > 0) {
				   jsonRes.addProperty("message", "Customer Updated Successfully");
				   json = gson.toJson(jsonRes);
			   }
			   else {
				   res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	               json = "Error While Updating Customer Data";
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
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")) {
			json = "error : Invalid Request, Customer ID needed to delete the customer";
		}
		else {
			String[] pathParts = pathInfo.split("/");

			if(pathParts.length == 2) {
				int rows = CustomerDAO.deleteCustomerById(Integer.parseInt(pathParts[1]));
				if(rows > 0){
					res.setStatus(HttpServletResponse.SC_OK);
					json = "message : Customer deleted successfully";
				}
				else {
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					json = "error : Customer Not Deleted";
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
