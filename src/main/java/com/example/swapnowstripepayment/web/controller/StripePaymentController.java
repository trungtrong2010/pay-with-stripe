package com.example.swapnowstripepayment.web.controller;

import com.example.swapnowstripepayment.entity.StripePayment;
import com.example.swapnowstripepayment.serivce.StripePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/stripe")
public class StripePaymentController {

    @Autowired
    private StripePaymentService stripePaymentService;

    @GetMapping
    public ResponseEntity<StripePayment> getStripePayment() {
        return new ResponseEntity<>(this.stripePaymentService.getStripePayment(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StripePayment> update(@RequestBody StripePayment stripePayment) {
        return new ResponseEntity<>(this.stripePaymentService.update(stripePayment), HttpStatus.OK);
    }
}
