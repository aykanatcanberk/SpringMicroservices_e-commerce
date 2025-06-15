package com.canberk.ecommerce.kafka;

import com.canberk.ecommerce.customer.CustomerResponse;
import com.canberk.ecommerce.entity.PaymentMethod;
import com.canberk.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}