package com.example.inventory.servlet;

import com.example.inventory.dao.ReportDAO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
public class ReportServlet extends HttpServlet {
    String json;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            setBadStatus(res);
        }
        else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                json = ReportDAO.getReport(pathParts[1]);
                if(json.isEmpty()){
                    setBadStatus(res);
                }
            } else {
                setBadStatus(res);
            }
        }
        res.setContentType("application/json");
        res.getWriter().write(json);
    }
    private void setBadStatus(HttpServletResponse res){
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        json = "error : Invalid Request";
    }
}
