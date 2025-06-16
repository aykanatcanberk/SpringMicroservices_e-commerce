package com.canberk.ecommerce.dto;

import com.canberk.ecommerce.entity.Customer;
import com.canberk.ecommerce.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}