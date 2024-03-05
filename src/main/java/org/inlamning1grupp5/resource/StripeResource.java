package org.inlamning1grupp5.resource;

import java.util.HashMap;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.inlamning1grupp5.model.Guest;
import org.inlamning1grupp5.model.StripeModel;
import org.inlamning1grupp5.service.StripeService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.ProductCollection;
import com.stripe.param.PaymentIntentCreateParams;
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
        ProductListParams params = ProductListParams.builder().setLimit(3L).build();
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
}
