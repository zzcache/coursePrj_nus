package myWeatherappl;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.PrintWriter;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;



@WebServlet(urlPatterns="/weather", asyncSupported=true)

public class weatherServlet extends HttpServlet {
  
    private static final String WEATHER_URL = "http://openweathermap.org/data/2.5/weather";
    private static final String APPID = "b6907d289e10d714a6e88b30761fae22";
    private Client client;
    
    @Override
    public void init () throws ServletException {
        client = ClientBuilder.newClient();
    }

    @Override
    public void destroy () {
        client.close();
    }
    
    protected void processRequest (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet myWeaterQuery</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet myWeaterQuery at " + request.getContextPath() + "</h1>");
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
            String cityName = request.getParameter("city");
            
            // create target
            WebTarget target = client.target(WEATHER_URL);
            target = target.queryParam("q", cityName)
                            .queryParam("appid", APPID)
                            .queryParam("units","metric");
            
            // Create Invocateion
            Invocation.Builder inv = target.request(MediaType.APPLICATION_JSON);
            
            //get the aysnc context
            AsyncContext asynCtx = request.startAsync(request, response);
            
            WeatherServletRunnable runnable = new WeatherServletRunnable(inv, asynCtx);
            asynCtx.start(runnable);
            
            System.out.println(">> we are exting the servlet");
   /*         
            // make the call using GET HTTT method
            JsonObject result = inv.get(JsonObject.class);
            
            JsonArray weatherDetails = result.getJsonArray("weather");
            log("RESULT: " + result);
            System.out.print("RESULT: " + result);
            
            
            if ( (cityName == null) || (cityName.trim().length() < 2) ) {
                cityName = "Singapore";
            }
            
             response.setStatus(HttpServletResponse.SC_OK);
             
            response.setContentType(MediaType.TEXT_XML);
            // response.setContentType(MediaType.TEXT_PLAIN);
             
             try (PrintWriter pw = response.getWriter()) {
                 pw.print("<h2> weather for city " + cityName + " </h2>");
                 for (int i = 0; i < weatherDetails.size() ; i++) {
                    JsonObject wd = weatherDetails.getJsonObject(i);
                    String main =  wd.getString("main");
                    String description =  wd.getString("description");
           //         String icon =  wd.getString("icon");
 //                   pw.print("<div>");
                    pw.print("<h3> " + main + " AAA " + description + " </h3>");
            //        pw.printf("<img src=\"http://openweathermap.org/img/w/%s.png\">", icon);                   
 //                   pw.print("</div>");
                 }
             }
             
     */        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletExceptin if a servlet-specific error occurs
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
