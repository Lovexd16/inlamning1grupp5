package org.inlamning1grupp5.resource;

import java.util.List;

import org.inlamning1grupp5.model.Customer;
import org.inlamning1grupp5.service.CustomerService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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


}
