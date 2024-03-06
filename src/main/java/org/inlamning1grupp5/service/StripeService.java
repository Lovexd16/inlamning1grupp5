package org.inlamning1grupp5.service;

import java.util.HashMap;

import org.inlamning1grupp5.model.Guest;
import org.inlamning1grupp5.model.User;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerCreateParams.Address;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class StripeService {

    @Inject
    UserService userService;

    public Response oneTimePurchaseAsGuest(String productId, Guest customer) throws StripeException {
        Product product = Product.retrieve(productId);
        Price price = Price.retrieve(product.getDefaultPrice());

        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(customer.getFirstName() + " " + customer.getLastName())
                .setEmail(customer.getEmail())
                .setAddress(Address.builder()
                    .setCity(customer.getCity())
                    .setLine1(customer.getAddress1())
                    .setLine2(customer.getAddress2())
                    .setPostalCode(customer.getPostnumber())
                    .build())
                .build();
            Customer customerFinal = Customer.create(customerParams);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(price.getUnitAmount())
            .setCurrency("SEK")
            .setDescription(product.getName())
            .setCustomer(customerFinal.getId())
            .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .build())
            .build();
        try {
            PaymentIntent intent = PaymentIntent.create(params);

            HashMap<String, String> clientSecretResp = new HashMap<>();
            clientSecretResp.put("clientSecret", intent.getClientSecret());
            return Response.ok(clientSecretResp).build();
        } catch (StripeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    public Response oneTimePurchaseByLogin(String productId, String username, String password, Guest customer) throws StripeException {
        Product product = Product.retrieve(productId);

        Boolean authenticateCustomer = userService.verifyUser(username, password);

        if (authenticateCustomer == true) {
            System.out.println(username + "!!!!!!!!");

            Price price = Price.retrieve(product.getDefaultPrice());

            Response findCustomer = userService.findUserByUsername(username);
            User customerFound = (User) findCustomer.getEntity();
            String customerName = customerFound.getFirstName() + " " + customerFound.getLastName();
            
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(customerName)
                .setEmail(customerFound.getEmail())
                .setAddress(Address.builder()
                    .setCity(customer.getCity())
                    .setLine1(customer.getAddress1())
                    .setLine2(customer.getAddress2())
                    .setPostalCode(customer.getPostnumber())
                    .build())
                .build();
            Customer customerFinal = Customer.create(customerParams);
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(price.getUnitAmount())
                .setCurrency("SEK")
                .setDescription(product.getName())
                .setCustomer(customerFinal.getId())
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build())
                .build();
            try {
                PaymentIntent intent = PaymentIntent.create(params);
    
                HashMap<String, String> clientSecretResp = new HashMap<>();
                clientSecretResp.put("clientSecret", intent.getClientSecret());
                return Response.ok(clientSecretResp).build();
            } catch (StripeException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password").build();
        }
    }
    
}
