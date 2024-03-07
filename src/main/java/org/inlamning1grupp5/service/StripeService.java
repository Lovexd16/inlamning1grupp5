package org.inlamning1grupp5.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.inlamning1grupp5.model.Guest;
import org.inlamning1grupp5.model.StripeModel;
import org.inlamning1grupp5.model.User;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerCreateParams.Address;
import com.stripe.param.SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.SubscriptionCreateParams;

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

    @Transactional(Transactional.TxType.REQUIRED)
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
                System.out.println(customerFound.getUserPurchaseHistory());
                customerFound.getUserPurchaseHistory().add(productId);
                System.out.println(customerFound.getUserPurchaseHistory());
                return Response.ok(clientSecretResp).build();
            } catch (StripeException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response activateSubscription(String productId, String username, String password, Guest address) throws StripeException {
        
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
                    .setCity(address.getCity())
                    .setLine1(address.getAddress1())
                    .setLine2(address.getAddress2())
                    .setPostalCode(address.getPostnumber())
                    .build())
                .build();
            Customer customerFinal = Customer.create(customerParams);

            try {
            SubscriptionCreateParams.PaymentSettings paymentSettings = 
                SubscriptionCreateParams.PaymentSettings.builder()
                .setSaveDefaultPaymentMethod(SaveDefaultPaymentMethod.ON_SUBSCRIPTION)
                .build();

            SubscriptionCreateParams subCreateParams = 
                SubscriptionCreateParams.builder()
                .setCustomer(customerFinal.getId())
                .addItem(SubscriptionCreateParams.Item.builder()
                    .setPrice(price.getId())
                    .build())
                .setPaymentSettings(paymentSettings)
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .addAllExpand(Arrays.asList("latest_invoice.payment_intent"))
                .build();

            Subscription subscription = Subscription.create(subCreateParams);

            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("subscriptionId", subscription.getId());
            responseData.put("clientSecret", subscription.getLatestInvoiceObject().getPaymentIntentObject()
                .getClientSecret());
            
            customerFound.setSubscribed(subscription.getId());
            return Response.ok(responseData).build();

            } catch (StripeException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password.").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response cancelSubscription(String username, String password) {
        Stripe.apiKey = StripeModel.getApiKey();
        Boolean authenticateCustomer = userService.verifyUser(username, password);
        if (authenticateCustomer == true) {
            System.out.println(1);
            Response response = userService.getUserAccount(username, password);
            User user = (User) response.getEntity();
            System.out.println(user.getSubscribed());
            if (user.getSubscribed() != "Not subscribed") {
                System.out.println(2);
                try {
                    System.out.println(3);
                    Subscription resource = Subscription.retrieve(user.getSubscribed());
                    SubscriptionCancelParams params = SubscriptionCancelParams.builder().build();
                    Subscription subscription = resource.cancel(params);
                    user.setSubscribed("Not subscribed");
                    return Response.ok(subscription).entity("Successfully cancelled subscription.").build();
                } catch (StripeException e) {
                    System.out.println(4);
                    return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
                }
            } else {
                System.out.println(5);
                return Response.status(Response.Status.NOT_FOUND).entity("You are not subscribed!").build();
            }
        } else {
            System.out.println(6);
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password.").build();
        }
    }
}
