package com.example.swapnowstripepayment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripePayment {

    @Id
    private int id;

    private String keyApi;
    private String keyPublic;
    private String keyWebhook;

    public StripePayment(StripePayment stripePayment) {
        this.keyApi = stripePayment.getKeyApi();
        this.keyPublic = stripePayment.getKeyPublic();
        this.keyWebhook = stripePayment.getKeyWebhook();
    }
}
