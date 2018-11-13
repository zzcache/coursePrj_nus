/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nus.day02.web;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import javax.ws.rs.core.MediaType;

@WebServlet(urlPatterns = {"/customer-sql"})
public class CustomerSQLServlet extends HttpServlet{
    
    @Resource(lookup = "jdbc/sample")
    private DataSource sampleDS;
    
    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        Integer custId = Integer.parseInt(req.getParameter("custId"));
        try ( Connection conn = sampleDS.getConnection() ) {
            PreparedStatement ps = conn.prepareStatement ( "select * from customer where customer_id = ?");
            
            ps.setInt(1, custId);
            ResultSet  rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("nus.day02.web.CustomerSQLServlet.doGet()");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType(MediaType.TEXT_PLAIN);
                try (PrintWriter pw = resp.getWriter()) {
                    pw.printf(" id: %id, name: %s, address: %s, phone: %s email: %s \n", 
                            rs.getInt("customer_id"), rs.getString("name"),
                            rs.getString("address"), rs.getString("phone"),
                            rs.getString("email"));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType(MediaType.TEXT_PLAIN);
                try (PrintWriter pw = resp.getWriter()) {
                    pw.print("NOT FOUND");
                }
                
            }
        } catch (SQLException ex) {
            log(ex.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType(MediaType.TEXT_PLAIN);
            try (PrintWriter pw = resp.getWriter()) {
                pw.println(ex.getMessage());
            }
        }
    }
    
    
}
