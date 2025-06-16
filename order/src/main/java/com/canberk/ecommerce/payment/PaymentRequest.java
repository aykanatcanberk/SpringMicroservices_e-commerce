package com.canberk.ecommerce.payment;

import com.canberk.ecommerce.customer.CustomerResponse;
import com.canberk.ecommerce.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}