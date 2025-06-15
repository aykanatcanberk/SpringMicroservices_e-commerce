package com.canberk.ecommerce.service;

import com.canberk.ecommerce.customer.CustomerClient;
import com.canberk.ecommerce.dto.OrderLineRequest;
import com.canberk.ecommerce.dto.OrderRequest;
import com.canberk.ecommerce.dto.OrderResponse;
import com.canberk.ecommerce.exception.BusinessException;
import com.canberk.ecommerce.mapper.OrderMapper;
import com.canberk.ecommerce.product.ProductClient;
import com.canberk.ecommerce.product.PurchaseRequest;
import com.canberk.ecommerce.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;

    public List<OrderResponse> findAllOrders() {
        return null;
    }

    public OrderResponse findById(Integer orderId) {
        return null;
    }

    public Integer createOrder(@Valid OrderRequest request) {

        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Order cannot created. Customer not found with id " + request.customerId()));

        this.productClient.purchaseProducts(request.products());

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
        return null;
    }
}
