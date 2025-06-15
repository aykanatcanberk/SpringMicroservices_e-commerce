package com.canberk.ecommerce.service;

import com.canberk.ecommerce.customer.CustomerClient;
import com.canberk.ecommerce.dto.OrderLineRequest;
import com.canberk.ecommerce.dto.OrderRequest;
import com.canberk.ecommerce.dto.OrderResponse;
import com.canberk.ecommerce.exception.BusinessException;
import com.canberk.ecommerce.kafka.OrderConfirmation;
import com.canberk.ecommerce.kafka.OrderProducer;
import com.canberk.ecommerce.mapper.OrderMapper;
import com.canberk.ecommerce.product.ProductClient;
import com.canberk.ecommerce.product.PurchaseRequest;
import com.canberk.ecommerce.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::fromOrder).collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.orderRepository.findById(id)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("There is no order with the provided ID: %d", id)));
    }

    public Integer createOrder(@Valid OrderRequest request) {

        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Order cannot created. Customer not found with id " + request.customerId()));

        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        var order = this.orderRepository.save(orderMapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }
}
