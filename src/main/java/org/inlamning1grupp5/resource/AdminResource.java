package org.inlamning1grupp5.resource;

import org.inlamning1grupp5.service.AdminService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;

@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {
    
    @Inject
    AdminService adminService;

    @GET
    @Path("/get-all-admins")
    public Response getAllAdmins() {
        return Response.ok(adminService.findAll()).build();
    }
    
    @GET
    @Path("/login")
    public Response adminAccount(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        
        return adminService.getAdminAccount(email, password);
    }
}

