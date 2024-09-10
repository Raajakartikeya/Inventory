package com.example.inventory.servlet;

import com.example.inventory.dao.PriceListDAO;
import com.example.inventory.model.PriceList;
import com.example.inventory.model.PriceListName;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PriceListServlet extends HttpServlet {
    JsonObject data = new JsonObject();
    String json;
    Gson gson = new Gson();
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PriceListName priceList = new PriceListName();
        data = new Gson().fromJson(req.getReader(), JsonObject.class);
        priceList.setPriceListName(data.get("priceListName").getAsString());
        JsonArray priceListItems = data.get("priceListItems").getAsJsonArray();
        List<PriceList> prices = new ArrayList<PriceList>();
        for(JsonElement jsonElement : priceListItems) {
            PriceList price = new PriceList();
            price.setItemId(((JsonObject) jsonElement).get("itemId").getAsInt());
            price.setItemPrice(((JsonObject) jsonElement).get("itemPrice").getAsDouble());
            prices.add(price);
        }
        priceList.setPriceList(prices);
        int rows = PriceListDAO.addNewPriceList(priceList);
        JsonObject jsonObject = new JsonObject();
        if(rows > 0) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            jsonObject.addProperty("message", "Price List added Successfully");
        }
        else {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonObject.addProperty("error", "Price List Not Added");
        }
        json = gson.toJson(jsonObject);
        res.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
