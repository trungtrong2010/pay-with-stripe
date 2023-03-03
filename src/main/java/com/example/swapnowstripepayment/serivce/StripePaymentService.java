package com.example.swapnowstripepayment.serivce;

import com.example.swapnowstripepayment.entity.StripePayment;
import com.example.swapnowstripepayment.repository.StripePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {

    @Autowired
    private StripePaymentRepository stripePaymentRepository;

    @Autowired
    private EncodeService encodeService;

    public String getKeyApi() {
        return this.encodeService.decodeData(this.stripePaymentRepository.getKeyApi());
    }

    public String getKeyPublic() {
        return this.encodeService.decodeData(this.stripePaymentRepository.getKeyPublic());
    }

    public String getKeyWebhook() {
        return this.encodeService.decodeData(this.stripePaymentRepository.getKeyWebhook());
    }

    public StripePayment getStripePayment() {
        StripePayment stripePayment = this.stripePaymentRepository.findById(1).get();
        stripePayment.setKeyApi(this.encodeService.decodeData(stripePayment.getKeyApi()));
        stripePayment.setKeyPublic(this.encodeService.decodeData(stripePayment.getKeyPublic()));
        stripePayment.setKeyWebhook(this.encodeService.decodeData(stripePayment.getKeyWebhook()));
        return stripePayment;
    }

    public StripePayment update(StripePayment stripePayment) {
        StripePayment stripePaymentShow = new StripePayment(stripePayment);
        stripePayment.setId(1);
        stripePayment.setKeyApi(this.encodeService.encodeData(stripePayment.getKeyApi()));
        stripePayment.setKeyPublic(this.encodeService.encodeData(stripePayment.getKeyPublic()));
        stripePayment.setKeyWebhook(this.encodeService.encodeData(stripePayment.getKeyWebhook()));
        this.stripePaymentRepository.save(stripePayment);
        return stripePaymentShow;
    }


}
