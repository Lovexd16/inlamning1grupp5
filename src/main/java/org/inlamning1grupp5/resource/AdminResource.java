package org.inlamning1grupp5.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.inlamning1grupp5.service.AdminService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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

    // @GET
    // @Path("/get-all-admins")
    // public Response getAllAdmins() {
    //     return Response.ok(adminService.findAll()).build();
    // }
    
    @GET
    @Path("/login")
    public Response adminAccount(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        
        return adminService.getAdminAccount(email, password);
    }

    @GET
    @Operation(
        summary = "See all users",
        description = "Collect a JSON object of all the users currently in the database"
    )
    @Path("/get-all-users")
    public Response getAllUsers(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        return adminService.findAllUsers(email, password);
    }

    @DELETE
    @Path("/delete-user")
    public Response deleteUser(@HeaderParam("email") String email, @HeaderParam("password") String password, @HeaderParam("username") String username) {
        return adminService.deleteUserAccount(email, password, username);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count-users")
    public Response countUsers(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        return adminService.countAllUsers(email, password);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count-subscribers")
    public Response countSubscribers(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        return adminService.countAllSubscribers(email, password);
    }
}

