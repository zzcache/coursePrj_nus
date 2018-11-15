package myWeatherappl;

import java.io.PrintWriter;
import javax.json.JsonObject;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

public class WeatherServletRunnable implements Runnable{
    private Invocation.Builder invcation;
    private AsyncContext asyncCtx;
    public WeatherServletRunnable(Invocation.Builder inv, AsyncContext ctx) {
        invcation = inv;
        this.asyncCtx = ctx;
    }
    
    @Override
    public void run () {
        JsonObject weather = invcation.get(JsonObject.class);
        HttpServletResponse resp = (HttpServletResponse) asyncCtx.getResponse();
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(MediaType.APPLICATION_JSON);
        try {
            try (PrintWriter pw = resp.getWriter()) {
                pw.print(weather.toString());
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
}
