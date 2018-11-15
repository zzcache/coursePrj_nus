
package nus.day04.rest;

import java.sql.SQLException;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import nus.day04.business.CustomerBean;
import nus.day04.model.Customer;

public class FindByCustomerIdRunnable implements Runnable{
    
    private CustomerBean customerBean;
    private Integer custId;
    private AsyncResponse asyncResp;

    public  FindByCustomerIdRunnable( Integer cid, CustomerBean cb, AsyncResponse ar) {
        custId = cid;
        customerBean = cb;
        asyncResp = ar;
    }
    
    @Override
    public void run() {
        Optional<Customer> opt = null;
        try {
            opt = customerBean.findByCsutomerId(custId);
        } catch (SQLException ex) {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            //500 Server Error
            asyncResp.resume(Response.serverError().entity(error).build());
            return;
        }
        // return 404 Not found if customer not exist
        if (!opt.isPresent()) {
            asyncResp.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        // Return the data as JSON
        asyncResp.resume(Response.ok(opt.get().toJson()).build());
        
        System.out.println(">>> resuming request");
    }
}
