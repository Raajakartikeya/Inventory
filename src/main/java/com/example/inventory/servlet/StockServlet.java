package com.example.inventory.servlet;

import com.example.inventory.dao.StockDAO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class StockServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        JsonArray jsonArray = StockDAO.getItemStocks();
        String json = new Gson().toJson(jsonArray);
        res.setContentType("application/json");
        res.getWriter().write(json);
    }
}
