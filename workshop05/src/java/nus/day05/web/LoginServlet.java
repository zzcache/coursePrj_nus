
package nus.day05.web;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import nus.day05.KeyManager;

@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

    @Inject private KeyManager keyMgr;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        try {
            req.login(username, password);
        } catch (ServletException ex) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType(MediaType.TEXT_PLAIN);
            try (PrintWriter pw = resp.getWriter()) {
                pw.println("Incorrect login");
            }
            return;
        }
        
        // we have successfully login
        // Genearte token
        // Create our cluset claims
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", req.getRemoteUser());
        claims.put("role", "authenticated");
        claims.put("host", req.getRemoteHost());
        claims.put("port", req.getRemotePort());
        
        long exp = System.currentTimeMillis() + (1000 * 60 * 30);
  // Token would be expired in 30 sec
  //      long exp = System.currentTimeMillis() + (1000 * 30);  
        
        String token = Jwts.builder()
                .setAudience("workshop05")
                .setExpiration(new Date(exp))
                .setIssuer("workshop05")
                .addClaims(claims)
                .signWith(keyMgr.getKey())
                .compact();
        
        JsonObject result = Json.createObjectBuilder()
                .add("token_typ", "bearer")
                .add("token", token)
                .build();
        
        System.out.println(">>> generate token" + result.toString());
        
                //Logout of session
        HttpSession sess = req.getSession();
        sess.invalidate();
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter pw = resp.getWriter()){
            pw.print(result.toString());
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
