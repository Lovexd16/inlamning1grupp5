package org.inlamning1grupp5.resource;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
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
    @Path("/create-user-account") 
    public Response createUser(@RequestBody User user) {

        try {
            return userService.createNewUser(user);
        } catch (Exception e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).entity("There was a problem creating your account").build();
        }
    }

    @GET
    @Path("/login")
    public Response userAccount(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        
        return userService.getUserAccount(username, password);
    }

    @DELETE
    @Path("/delete-user-account")
    public Response deleteUser(@HeaderParam("username") String username, @HeaderParam("password") String password) {

        return userService.deleteUserAccount(username, password);
    }

    @PATCH
    @Path("/edit-user-account")
    public Response editUser(@HeaderParam("username") String username, @HeaderParam("password") String password, @RequestBody User user) {

        System.out.println(user.getEmail());
        return userService.editUserAccount(username, password, user);
    }


}
