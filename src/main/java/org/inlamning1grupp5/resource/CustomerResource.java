package org.inlamning1grupp5.resource;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.inlamning1grupp5.model.Customer;
import org.inlamning1grupp5.service.CustomerService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    
    @Inject
    CustomerService customerService;

    @GET
    @Path("/get-all-customers")
    public Response getAllCustomers() {
        return Response.ok(customerService.findAll()).build();
    }

    @POST
    @Path("/create-customer-account") 
    public Response createCustomer(@RequestBody Customer customer) {

        try {
            return customerService.createNewCustomer(customer);
        } catch (Exception e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).entity("There was a problem creating your account").build();
        }
    }

    @GET
    @Path("/login")
    public Response customerAccount(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        
        return customerService.getCustomerAccount(username, password);
    }

    @DELETE
    @Path("/delete-customer-account")
    public Response deleteCustomer(@HeaderParam("username") String username, @HeaderParam("password") String password) {

        return customerService.deleteCustomerAccount(username, password);
    }


}
