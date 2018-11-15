
package nus.day04.rest;

import java.sql.SQLException;
import java.util.Optional;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nus.day04.business.CustomerBean;
import nus.day04.model.Customer;


@RequestScoped
@Path("/customer")
public class CustomerResource {
 
    @EJB private CustomerBean customerBean;
    
    // Note Please create the threadpool in system(server) admin console
    @Resource(lookup = "concurrent/myTracepool")
    private ManagedScheduledExecutorService threadPool;    
            
    
    // GET /customer
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{custId}")
    public Response findByCustomerId (@PathParam("custId") Integer custId) {
        
        Optional<Customer> opt = null;
        try {
            opt = customerBean.findByCsutomerId(custId);
        } catch (SQLException ex) {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            //500 Server Error
            return Response.serverError().entity(error).build();
        }
        // return 404 Not found if customer not exist
        if (!opt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Return the data as JSON
        return Response.ok(opt.get().toJson()).build();
    }
    
    // GET /customer/async/1
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("async/{custId}")
    public void findByAsyncCustomerId (@PathParam("custId") Integer custId,
        @Suspended AsyncResponse asyncResp) {
        FindByCustomerIdRunnable runnable = new FindByCustomerIdRunnable(custId, customerBean, asyncResp);
        
        // excute the runnable in the threadpool
        threadPool.execute(runnable);
        System.out.println(">>> exting findByAsyncCustomerId");
    }

}
