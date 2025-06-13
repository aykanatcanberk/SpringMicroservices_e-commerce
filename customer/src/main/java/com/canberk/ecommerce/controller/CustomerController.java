package com.canberk.ecommerce.controller;

import com.canberk.ecommerce.customer.Customer;
import com.canberk.ecommerce.dto.CustomerRequest;
import com.canberk.ecommerce.dto.CustomerResponse;
import com.canberk.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {

        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    @GetMapping("exits/{customer-id}")
    public ResponseEntity<Boolean> exitsById(@PathVariable("customer-id") String customerId) {

        return ResponseEntity.ok(customerService.exitsById(customerId));
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId) {

        return ResponseEntity.ok(customerService.findCustomerById(customerId));
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request) {

        return ResponseEntity.ok(customerService.createCostumer(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerRequest request) {
        customerService.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customer-id") String customerId) {

        customerService.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }
}
