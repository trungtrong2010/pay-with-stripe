package com.example.swapnowstripepayment.web.controller;

import com.example.swapnowstripepayment.serivce.StripePaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class StripeWebhookController {

    @Autowired
    private StripePaymentService stripePaymentService;

    private final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    @PostMapping("/stripe/events")
    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {

        if (sigHeader == null) {
            return "";
        }
        Event event;

        // Only verify the event if you have an endpoint secret defined.
        // Otherwise use the basic event deserialized with GSON.
        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, this.stripePaymentService.getKeyWebhook()
            );
        } catch (SignatureVerificationException e) {
            // Invalid signature
            logger.info("⚠️  Webhook error while validating signature.");

            return "";
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        // Handle the event
        System.out.println(event.getType());
        switch (event.getType()) {
            case "charge.succeeded":
                JSONObject objectEvent1 = new JSONObject(event);
//              lấy username gán vào database
                String username1 = objectEvent1.getJSONObject("data").getJSONObject("object").getJSONObject("metadata").get("username").toString();
                if (username1 != null) {
                    System.out.println("Thanh toán 1 lần");
                    System.out.println("username: " + username1);
                }
                break;
            case "checkout.session.completed":
                JSONObject objectEvent = new JSONObject(event);
//              lấy username gán vào database
                String username = objectEvent.getJSONObject("data").getJSONObject("object").getJSONObject("metadata").get("username").toString();
                if (username != null) {
                    System.out.println("thanh toán tự động");
                    System.out.println("username: " + username);
                }
                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
        return "success";
    }
}
