package org.inlamning1grupp5.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.inlamning1grupp5.model.User;
import org.inlamning1grupp5.service.UserService;


import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    UserService userService;

    @POST
    @Operation(summary = "Create a user.", description = "Create a user and save it in the database.")

    @APIResponse(responseCode = "200", description = "User created successfully.")
    @APIResponse(responseCode = "400", description = "The user has not entered a password")
    @APIResponse(responseCode = "409", description = "Email or username is already in use.")
    @APIResponse(responseCode = "500", description = "You must enter firstname(1-30),lastname(1-30), username(5-15), password(no limit) and email(email format).")

    @Path("/create-user-account") 
    public Response createUser(@RequestBody User user) {
        
        if (user.getPassword() != null && user.getPassword().trim() != "") {
        try {
                return userService.createNewUser(user);
            } catch (Exception e) {
                System.out.println(e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("There was a problem creating your account.").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("You must choose a password.").build();
        }
    }

    @GET
    @Operation(summary = "Login as user", description = "Login as a user that is already saved in the database.")

    @APIResponse(responseCode = "200", description = "Logged in successfully as a user.")
    @APIResponse(responseCode = "404", description = "Incorrect username or password.")
    

    @Path("/login")
    public Response userAccount(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        
        return userService.getUserAccount(username, password);
    }

    @DELETE
    @Operation(summary = "Delete your account as a user", description = "Delete your account as a user and remove it from the database.")

    @APIResponse(responseCode = "200", description = "Account deleted successfully as a user.")
    @APIResponse(responseCode = "404", description = "Incorrect username or password.")

    @Path("/delete-user-account")
    public Response deleteUser(@HeaderParam("username") String username, @HeaderParam("password") String password) {

        return userService.deleteUserAccount(username, password);
 
    } 

    @PATCH
    @Operation(summary = "Edit your account as a user", description = "Replace the current information with the new info and save it in the database.")

    @APIResponse(responseCode = "200", description = "Edited account successfully as a user.")
    @APIResponse(responseCode = "404", description = "Incorrect username or password.")

    @Path("/edit-user-account")
    public Response editUser(@HeaderParam("username") String username, @HeaderParam("password") String password, @RequestBody User user) {

        System.out.println(user.getEmail());
        return userService.editUserAccount(username, password, user);
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("get-podcast")
    public Response getPodcast(@HeaderParam("productId") String productId) {

        return userService.getPodcastFromServer(productId);   
    }
}
