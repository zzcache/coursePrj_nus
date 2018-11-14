
package nus.day02.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nus.day02.business.CustomerBean;
import nus.day02.model.Customer;
import nus.day02.model.CustomerException;
import nus.day02.model.DiscountCode;

@WebServlet(urlPatterns = {"/customer"})
public class CustomerSerlet extends HttpServlet {
    
    @EJB private CustomerBean customerBean;
    
    private Customer CreateCustomer(HttpServletRequest req) {
        Customer customer = new Customer();
        customer.setCustomerId(Integer.parseInt(req.getParameter("customerId")));
        customer.setName(req.getParameter("name"));
        customer.setZip(req.getParameter("zip"));
        customer.setAddressline1(req.getParameter("addressline1"));
        customer.setAddressline2(req.getParameter("addressline2"));
        customer.setCity(req.getParameter("city"));
        customer.setState(req.getParameter("state"));
        customer.setPhone(req.getParameter("phone"));
        customer.setFax(req.getParameter("fax"));
        customer.setEmail(req.getParameter("email"));
        
        DiscountCode dc = new DiscountCode();
        dc.setDiscountCode(DiscountCode.Code.valueOf(req.getParameter("discountcode")));
        customer.setDiscountCode(dc);
        
        customer.setCreditLimit(Integer.parseInt(req.getParameter("creditlimit")));
        
        return (customer);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CustomerSerlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerSerlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer customerId = Integer.parseInt(req.getParameter("customerId"));
        
        // get an optional obj which indicate the obj may or may not exit
        // check if we have no result 
        Optional<Customer> opt = customerBean.findByCustomerId(customerId);
        if (!opt.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("text/plain");
            try (PrintWriter pw = resp.getWriter()) {
                pw.printf("Customer id %d does not exist\n", customerId);
            }
            return;
        }
        // if we get result
        Customer customer = opt.get();
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        try (PrintWriter pw = resp.getWriter()) {
            pw.print(customer.toJson());
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
         Customer customer = CreateCustomer(req);
         
         Optional<Customer> opt = customerBean.findByCustomerId(customer.getCustomerId());
         if(opt.isPresent()) {
               resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
               resp.setContentType("text/plain");
               try (PrintWriter pw = resp.getWriter()) {
                   pw.printf("Customer id %d exists\n", customer.getCustomerId());
               }
               return;
         }

         try {
             customerBean.addNewCustomer(customer);
         } catch (CustomerException ex) {
             ex.printStackTrace();
               resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
               resp.setContentType("text/plain");
               try (PrintWriter pw = resp.getWriter()) {
                   pw.print(ex.getMessage());
               }
               return;
         }
           System.out.print(customer.toString());
           resp.setStatus(HttpServletResponse.SC_ACCEPTED);
           resp.setContentType("text/plain");
           try (PrintWriter pw = resp.getWriter()) {
               pw.print("Customer Created");
           }
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
