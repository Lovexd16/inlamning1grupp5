package org.inlamning1grupp5.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.inlamning1grupp5.service.AdminService;

import com.stripe.exception.StripeException;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
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
    @Operation(summary = "Login as admin", description = "Comparing admin login with existing admin account in the database. If it matches you will log in.")
    @APIResponse(responseCode = "200", description = "Login successful.")
    @APIResponse(responseCode = "404", description = "Wrong email or password.")
    @Path("/login")
    public Response adminAccount(@HeaderParam("email") @NotEmpty String email, @HeaderParam("password") @NotEmpty String password) {
        return adminService.getAdminAccount(email, password);
    }

    @GET
    @Operation(summary = "Get all users", description = "Get a JSON object of all the users currently in the database.")
    @APIResponse(responseCode = "200", description = "All users is shown.")
    @APIResponse(responseCode = "404", description = "Wrong email or password.")
    @Path("/get-all-users")
    public Response getAllUsers(@HeaderParam("email") @NotEmpty String email, @HeaderParam("password") @NotEmpty String password) {
        return adminService.findAllUsers(email, password);
    }

    @DELETE
    @Operation(summary = "Delete a user as admin.", description = "Delete a user from the database as admin.")
    @APIResponse(responseCode = "200", description = "Successfully deleted user as admin.")
    @APIResponse(responseCode = "404", description = "Wrong username, email or password.")
    @Path("/delete-user")
    public Response deleteUser(@HeaderParam("email") @NotEmpty String email, @HeaderParam("password") @NotEmpty String password, @HeaderParam("username") @NotEmpty String username) {
        return adminService.deleteUserAccount(email, password, username);
    }

    @GET
    @Operation(summary = "Count the users.", description = "Count the amount of users that are saved in the database.")
    @APIResponse(responseCode = "200", description = "Users successfully counted.")
    @APIResponse(responseCode = "404", description = "Wrong email or password.")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count-users")
    public Response countUsers(@HeaderParam("email") @NotEmpty String email, @HeaderParam("password") @NotEmpty String password) {
        return adminService.countAllUsers(email, password);
    }

    @GET
    @Operation(summary = "Count the subscribers.", description = "Count the amount of subscribers that are saved in the database.")
    @APIResponse(responseCode = "200", description = "Subscribers successfully counted.")
    @APIResponse(responseCode = "404", description = "Wrong email or password.")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count-subscribers")
    public Response countSubscribers(@HeaderParam("email") @NotEmpty String email, @HeaderParam("password") @NotEmpty String password) {
        return adminService.countAllSubscribers(email, password);
    }

    @GET
    @Path("/get-customer-by-id")
    public Response getCustomer(@HeaderParam("email") @NotEmpty String email, @HeaderParam("password") @NotEmpty String password, @HeaderParam("customerId") @NotEmpty String customerId) throws StripeException {
        return adminService.getCustomerById(email, password, customerId);
    }
}

