package com.example.swapnowstripepayment.web.controller;

import com.example.swapnowstripepayment.serivce.StripePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private StripePaymentService stripePaymentService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("stripePublicKey", this.stripePaymentService.getKeyPublic());
        model.addAttribute("urlCancel", CANCEL_URL);
        model.addAttribute("urlSuccess", SUCCESS_URL);
        return "checkout";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay() {
        return "success";
    }

//    Lưu thẻ trên web stripe
//    @GetMapping("/save-card")
//    public String saveCard(Model model) throws StripeException {
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("customer", "cus_MNKlCKjAUPipbh");
//        SetupIntent intent = SetupIntent.create(params);
//
//        Map<String, String> map = new HashMap();
//        System.out.println(intent.getClientSecret());
//        map.put("client_secret", intent.getClientSecret());
//        model.addAttribute("client_secret", intent.getClientSecret());
//        return "save_card";
//    }

}
