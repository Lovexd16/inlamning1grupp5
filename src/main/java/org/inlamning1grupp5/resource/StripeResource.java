package org.inlamning1grupp5.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.inlamning1grupp5.model.Guest;
import org.inlamning1grupp5.model.StripeModel;
import org.inlamning1grupp5.service.StripeService;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.ProductCollection;
import com.stripe.param.ProductListParams;

@Path("/api/customer/stripe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StripeResource {

    @Inject
    StripeService stripeService;
    
    @GET
    @Operation(summary = "Retrieve the Stripe publishable key.", description = "Retrieve the Stripe publishable key (text) for use with client-side communication with Stripe.")
    @APIResponse(responseCode = "200", description = "Publishable key retrieved successfully.")
    @APIResponse(responseCode = "500", description = "Unknown server error.")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/get-public-key")
    public Response getPublicKey() {
        String publicKey = "";
        return Response.ok(publicKey).build();
    }

    @GET
    @Operation(summary = "Retrieve all products from Stripe.", description = "Returns all products currently in your Stripe catalogue.")
    @APIResponse(responseCode = "200", description = "Products retrieved successfully.")
    @APIResponse(responseCode = "500", description = "Unknown server error.")
    @Path("/get-all-products")
    public Response getAllProducts() throws StripeException {
        Stripe.apiKey = StripeModel.getApiKey();
        ProductListParams params = ProductListParams.builder().setLimit(24L).build();
        ProductCollection products = Product.list(params);

        return Response.ok(products).build();
    }

    @GET
    @Operation(summary = "Retrieve a single product from Stripe.", description = "Retrieve a single product from the Stripe catalogue using the Stripe product ID.")
    @APIResponse(responseCode = "200", description = "Product retrieved successfully.")
    @APIResponse(responseCode = "404", description = "Product not found.")
    @APIResponse(responseCode = "500", description = "Unknown server error.")
    @Path("/get-single-product")
    public Response getSingleProduct(@HeaderParam("productId") String productId) throws StripeException {
        try {
            Stripe.apiKey = StripeModel.getApiKey();
            Product product = Product.retrieve(productId);
    
            return Response.ok(product).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Operation(summary = "Purchase a single product from Stripe.", description = "Purchase a single product from the Stripe catalogue using the Stripe product ID along with eithe log in credentials or guest information.")
    @APIResponse(responseCode = "200", description = "Successful purchase.")
    @APIResponse(responseCode = "400", description = "Something went wrong. please try again.")
    @APIResponse(responseCode = "403", description = "Incorrect username or password.")
    @APIResponse(responseCode = "500", description = "Unknown server error.")
    @Path("/one-time-purchase")
    public Response oneTimePurchase(@HeaderParam("productId") String productId, @HeaderParam("username") String username,
        @HeaderParam("password") String password, @RequestBody Guest customer) throws StripeException {
        
        Stripe.apiKey = StripeModel.getApiKey();

        if (username == null && password == null) {
            return stripeService.oneTimePurchaseAsGuest(productId, customer);
        } else {
            return stripeService.oneTimePurchaseByLogin(productId, username, password, customer);
        }
    }

    @POST
    @Operation(summary = "Subscribe to the podcast using Stripe.", description = "Subscribe to the podcast using stripe along with your log in credentials (no guest subscription available).")
    @APIResponse(responseCode = "200", description = "Successful purchase.")
    @APIResponse(responseCode = "400", description = "You need to enter a valid username and password.")
    @APIResponse(responseCode = "403", description = "Incorrect username or password.")
    @APIResponse(responseCode = "500", description = "Unknown server error.")
    @Path("/activate-subscription")
    public Response subcribe(@HeaderParam("productId") String productId, @HeaderParam("username") @NotEmpty String username,
    @HeaderParam("password") @NotEmpty String password, @RequestBody Guest address) throws StripeException {

        Stripe.apiKey = StripeModel.getApiKey();

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You need to enter your username and password!").build();
        } else {
            return stripeService.activateSubscription(productId, username, password, address);
        }
    }

    @PATCH
    @Path("/cancel-subscription")
    @Operation(summary = "Cancel a subscription to the podcast.", description = "Cancel a subscription to the podcast using your log in credentials.")
    @APIResponse(responseCode = "200", description = "Subscription successfully cancelled.")
    @APIResponse(responseCode = "400", description = "You need to enter a valid username and password.")
    @APIResponse(responseCode = "403", description = "Incorrect username or password.")
    @APIResponse(responseCode = "404", description = "You are not currently subscribed.")
    @APIResponse(responseCode = "417", description = "Problem communicating with Stripe. Please try again.")
    @APIResponse(responseCode = "500", description = "Unknown server error.")
    public Response cancelSubscription(@HeaderParam("username") @NotEmpty String username, @HeaderParam("password") String password) {

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You need to enter your password!").build();
        } else {
            return stripeService.cancelSubscription(username, password);
        }
    }

    @GET
    @Path("/get-product-price")
    @Operation(summary = "Get a specific product price from Stripe.", description = "Retrieve a product price from Stripe using the price ID (available from the product object - 'defaultPrice')")
    public Response getProductPrice(@HeaderParam("priceId") @NotEmpty String priceId) throws StripeException {
        return Response.ok(Price.retrieve(priceId)).build();
    }
}
