package com.canberk.ecommerce.mapper;

import com.canberk.ecommerce.dto.OrderRequest;
import com.canberk.ecommerce.entity.Order;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public Order toOrder( OrderRequest request) {

        return Order.builder()
                .id(request.id())
                .customerId(request.customerId())
                .reference(request.reference())
                .totalAmount(request.amount())
                .paymentMethod(request.paymentMethod())
                .build();
    }
}
