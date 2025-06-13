package com.canberk.ecommerce.dto;

import com.canberk.ecommerce.customer.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerResponse(

        String id,
        String firstName,
        String lastName,
        String email,
        Address address
) {
}
