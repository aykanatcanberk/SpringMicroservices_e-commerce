package com.canberk.ecommerce.dto;

public record OrderLineResponse(
        Integer id,
        double quantity
) { }