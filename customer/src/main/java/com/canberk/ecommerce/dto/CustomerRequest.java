package com.canberk.ecommerce.dto;

import com.canberk.ecommerce.customer.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
         String id,

         @NotNull(message = "Customer firstname is required.")
         String firstName,

         @NotNull(message = "Customer lastname is required.")
         String lastName,

         @NotNull(message = "Customer email is required.")
         @Email(message = "Valid email is required")
         String email,

         @NotNull(message = "Customer address is required.")
         Address address
) {
}
