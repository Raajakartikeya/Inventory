package com.example.inventory.servlet;

import java.io.IOException;

import com.example.inventory.dao.PaymentDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PaymentServlet extends HttpServlet{
	JsonObject data = new JsonObject();
	String json;
	Gson gson = new Gson();
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		String pathInfo = req.getPathInfo();
		data = new Gson().fromJson(req.getReader(), JsonObject.class);
		if(pathInfo == null || pathInfo.equals("/")) {
			json = "Need ID in the URL to Update";
		}
		else {
			String[] pathParts = pathInfo.split("/");
			JsonObject jsonObject = new JsonObject();
			if(pathParts.length == 2) {
				int rowsAffected = PaymentDAO.updatePayment(Integer.parseInt(pathParts[1]), data.get("payAmount").getAsDouble());
				if(rowsAffected == 1) {
					res.setStatus(HttpServletResponse.SC_CREATED);
					jsonObject.addProperty("message", "Payment Successful");
					json = gson.toJson(jsonObject);
				}
				else if(rowsAffected == 0){
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                json = "Invalid Request";
				}
				else {
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                json = "Paid Amount is greater than balance due";
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
