package com.example.swapnowstripepayment.repository;

import com.example.swapnowstripepayment.entity.StripePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StripePaymentRepository extends JpaRepository<StripePayment,Integer> {

    @Query(value = "select stripe_payment.key_api from stripe_payment where stripe_payment.id = 1", nativeQuery = true)
    String getKeyApi();

    @Query(value = "select stripe_payment.key_public from stripe_payment where stripe_payment.id = 1", nativeQuery = true)
    String getKeyPublic();

    @Query(value = "select stripe_payment.key_webhook from stripe_payment where stripe_payment.id = 1", nativeQuery = true)
    String getKeyWebhook();

}
