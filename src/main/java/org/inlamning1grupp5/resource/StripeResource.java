package org.inlamning1grupp5.resource;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.inlamning1grupp5.model.Guest;
import org.inlamning1grupp5.model.StripeModel;
import org.inlamning1grupp5.service.StripeService;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
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
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/get-public-key")
    public Response getPublicKey() {
        String publicKey = "pk_test_51OmBbZG7YXGZMv5ODxHfCK3Fuf6kXwfdu3jOvqR7lY84yLTwuaLWOP3Btaej3YJ7a11uqYIBKY6iv20wSZ5U7HxY0076nIcBI4";
        return Response.ok(publicKey).build();
    }

    @GET
    @Path("/get-all-products")
    public Response getAllProducts() throws StripeException {
        Stripe.apiKey = StripeModel.getApiKey();
        ProductListParams params = ProductListParams.builder().setLimit(24L).build();
        ProductCollection products = Product.list(params);

        return Response.ok(products).build();
    }

    @GET
    @Path("/get-single-product")
    public Response getSingleProduct(@HeaderParam("productId") String productId) throws StripeException {
        Stripe.apiKey = StripeModel.getApiKey();
        Product product = Product.retrieve(productId);

        return Response.ok(product).build();
    }

    @POST
    @Path("/one-time-purchase")
    public Response oneTimePurchase(@HeaderParam("productId") String productId, @HeaderParam("username") String username,
        @HeaderParam("password") String password, @RequestBody Guest customer) throws StripeException {
        
        Stripe.apiKey = StripeModel.getApiKey();

        if (username == null && password == null) {
            return stripeService.oneTimePurchaseAsGuest(productId, customer);
        } else {
            System.out.println("here: " + username + " " + password);
            return stripeService.oneTimePurchaseByLogin(productId, username, password, customer);
        }
    }

    @POST
    @Path("/activate-subscription")
    public Response subcribe(@HeaderParam("productId") String productId, @HeaderParam("username") @NotEmpty String username,
    @HeaderParam("password") @NotEmpty String password, @RequestBody Guest address) throws StripeException {

        Stripe.apiKey = StripeModel.getApiKey();

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You need to enter your username and password!").build();
        } else {
            System.out.println("here: " + username + " " + password);
            return stripeService.activateSubscription(productId, username, password, address);
        }
    }

    @PATCH
    @Path("/cancel-subscription")
    public Response cancelSubscription(@HeaderParam("username") @NotEmpty String username, @HeaderParam("password") @NotEmpty String password) {

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You need to enter your username and password!").build();
        } else {
            return stripeService.cancelSubscription(username, password);
        }
    }

    @GET
    @Path("/get-product-price")
    public Response getProductPrice(@HeaderParam("priceId") @NotEmpty String priceId) throws StripeException {
        return Response.ok(Price.retrieve(priceId)).build();
    }
}
