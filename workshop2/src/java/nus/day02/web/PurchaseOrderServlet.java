package nus.day02.web;

import nus.day02.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author issuser
 */
@WebServlet(urlPatterns = {"/purchaseOrder"})
public class PurchaseOrderServlet extends HttpServlet {
    
    @PersistenceContext private EntityManager em;
    
    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer custId = Integer.parseInt(req.getParameter("custId"));
        TypedQuery<PurchaseOrder> query = em.createNamedQuery("PurchaseOrder.findByCustomerId", PurchaseOrder.class);
        query.setParameter("custId", custId);
        List<PurchaseOrder> result = query.getResultList();
        
        if (result.size() <= 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType(MediaType.TEXT_PLAIN);
            try (PrintWriter pw = resp.getWriter()) {
                pw.print("NO RESULT FOUND!");
            }
        } else {
            
            JsonArrayBuilder builder = Json.createArrayBuilder();
            for (PurchaseOrder po: result) {
                builder.add(po.toJson());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            try (PrintWriter pw = resp.getWriter()) {
                pw.print(builder.build());
            }
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PurchaseOrderServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PurchaseOrderServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

   
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
