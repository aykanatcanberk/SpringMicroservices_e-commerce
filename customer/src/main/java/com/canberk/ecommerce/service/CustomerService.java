package com.canberk.ecommerce.service;

import com.canberk.ecommerce.customer.Customer;
import com.canberk.ecommerce.dto.CustomerRequest;
import com.canberk.ecommerce.dto.CustomerResponse;
import com.canberk.ecommerce.exception.CustomerNotFoundException;
import com.canberk.ecommerce.mapper.CustomerMapper;
import com.canberk.ecommerce.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public String createCostumer( CustomerRequest request) {

        var customer = customerRepository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {

        var customer = customerRepository.findById(request.id())
                .orElseThrow(()->new CustomerNotFoundException(
                        String.format("Customer with id %s not found", request.id())
                ));
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setAddress(request.address());

        customerRepository.save(customer);
    }

    public List<CustomerResponse> findAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean exitsById(String customerId) {
        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(mapper::fromCustomer)
                .orElseThrow(()->new CustomerNotFoundException((String.format("Customer with id %s not found", customerId))));
    }

    public void deleteCustomer(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException(
                        String.format("Customer with id %s not found", customerId)
                ));

        customerRepository.deleteById(customerId);
    }
}
