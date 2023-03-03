package com.example.swapnowstripepayment.web.controller;

import com.example.swapnowstripepayment.dto.CreatePayment;
import com.example.swapnowstripepayment.dto.CreatePaymentResponse;
import com.example.swapnowstripepayment.serivce.StripePaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.InvoiceFinalizeInvoiceParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

    @Autowired
    private StripePaymentService stripePaymentService;

    //    Dùng thanh toán hàng tháng (chỉ gửi được hóa đơn chưa tự dộng thanh toán)
    @GetMapping("/auto-renew")
    public ModelAndView autoRenew(Model model) throws StripeException {
        // cần thông tin khách hàng
        //username
        //giá tiền
        // api success, or api fail when payment

        String keyPrice = "price_1LfK79BaMROQd3OYQBjoJl1T";
        String username = "vtt";
        String email = "trungtrongcr21@gmail.com";

        Customer customer4 = new Customer();
        customer4.setEmail(email);

        Stripe.apiKey = this.stripePaymentService.getKeyApi();

        Map<String, String> initialMetadata = new HashMap<>();
        initialMetadata.put("username", username);

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(keyPrice).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION).setSuccessUrl("http://localhost:8083/success").setCancelUrl("http://localhost:8083/fail")
                .setCustomer(customer4.getId())
                .putAllMetadata(initialMetadata)
                .build();

        Session session = Session.create(params);

        return new ModelAndView("redirect:" + session.getUrl());
    }

    @GetMapping("/success")
    public String autoRenewSuccess() {
        return "Auto renew success";
    }

    @GetMapping("/fail")
    public String autoRenewFail() {
        return "Auto renew fail";
    }

    //    Api dùng thanh toán 1 lần, api này tự động được gọi khi client call api "/" in WebController
    @PostMapping("/create-payment-intent")
    public CreatePaymentResponse createPaymentIntent(@RequestBody CreatePayment createPayment) throws StripeException {
        // Alternatively, set up a webhook to listen for the payment_intent.succeeded event
        // and attach the PaymentMethod to a new Custom

        // Price = dollar
        int price = 2;

        // Truyền stripeApiKey (web stripe) vào thuộc tính apiKey của đối tượng Stripe để thanh toán
        Stripe.apiKey = this.stripePaymentService.getKeyApi();

        Map<String, String> initialMetadata = new HashMap<>();

        String username = "TrongVT"; // Get username of client

        initialMetadata.put("username", username);
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(price * 100L) // truyền giá tiền
                        .putAllMetadata(initialMetadata)
                        .setCurrency("usd") // Đơn giá cần thanh toán (link kí hiệu đơn giá: https://stripe.com/docs/currencies)
                        .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION) // Tắt chế độ lưu thẻ
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return new CreatePaymentResponse(paymentIntent.getClientSecret());
    }

    // Hủy đăng tự động thanh toán (auto renew) trên web stripe
    @GetMapping("/cancel-renew")
    public String cancleRenew() throws StripeException {

        // key của subscription trên web stripe
        String keyAutoRenewOfSubscription = "sub_1Lcn7TDvtNyXog4yzjMKljpU";

        String username = "test_username";

        Stripe.apiKey = this.stripePaymentService.getKeyApi();

        Map<String, String> initialMetadata = new HashMap<>();
        initialMetadata.put("username", username);

        Subscription subscription = Subscription.retrieve(keyAutoRenewOfSubscription);
        subscription.setMetadata(initialMetadata);
        subscription.cancel();
        return subscription.toJson();
    }

    //    Thanh toán hóa đơn thủ công từ client
    @GetMapping("invoice/pay-one")
    public ModelAndView invoice() throws StripeException {
        Stripe.apiKey = this.stripePaymentService.getKeyApi();

//        Id hóa đơn nháp chờ thanh toán
        Invoice resource = Invoice.retrieve("in_1LhU7uBaMROQd3OYZ8ZgzEZg");

        InvoiceFinalizeInvoiceParams params = InvoiceFinalizeInvoiceParams.builder().build();
        Invoice invoice = resource.finalizeInvoice(params);

        String url = invoice.getHostedInvoiceUrl();

        return new ModelAndView("redirect:" + url);
    }

    //    Thanh toán hóa đơn tự động
    @GetMapping("invoice/pay-auto/{id_invoice}")
    public Invoice payInvoice(@PathVariable("id_invoice") String idInvoice) throws StripeException {
        Stripe.apiKey = this.stripePaymentService.getKeyApi();

        Invoice invoice = Invoice.retrieve(idInvoice); // id_invoice
        Invoice updatedInvoice = invoice.pay();

        System.out.println(updatedInvoice);

        return updatedInvoice;
    }

    @GetMapping("/list-invoices-draft")
    public InvoiceCollection getAllInvoicesDraft() throws StripeException {
        Stripe.apiKey = this.stripePaymentService.getKeyApi();

        Map<String, Object> params = new HashMap<>();
        params.put("status", "draft");
        // limit là số lượng invoice,
        // Không add 'limit' mặc định = 10 element
        params.put("limit", 10000);

        InvoiceCollection invoices = Invoice.list(params);

        for (int i = 0; i < invoices.getData().size(); i++) {
            System.out.println(invoices.getData().get(i).getId()); // id invoice
            System.out.println(invoices.getData().get(i).getCustomerEmail());
        }
        System.out.println(invoices.getData().size());
        return invoices;
    }

}

