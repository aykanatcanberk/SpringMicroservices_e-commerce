package com.canberk.ecommerce.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmailTemplates {

    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment successfully processed"),
    ORDER_CONFIRMATION("order-confirmation.html", "Order confirmation")
    ;

    @Getter
    private final String template;
    @Getter
    private final String subject;

}