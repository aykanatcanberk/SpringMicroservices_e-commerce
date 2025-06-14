package com.canberk.ecommerce.service;

import com.canberk.ecommerce.dto.ProductPurchaseRequest;
import com.canberk.ecommerce.dto.ProductPurchaseResponse;
import com.canberk.ecommerce.dto.ProductRequest;
import com.canberk.ecommerce.dto.ProductResponse;
import com.canberk.ecommerce.exception.ProductPurchaseException;
import com.canberk.ecommerce.mapper.ProductMapper;
import com.canberk.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest request) {

        var product = productMapper.toProduct(request);
        productRepository.save(product);
        return product.getId();
    }

    public ProductResponse findById(Integer productId) {

        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {

        var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();

        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One ore more product does not exist.");
        }

        var storesRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchases = new ArrayList<ProductPurchaseResponse>();

        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storesRequest.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID:: " + productRequest.productId());
            }

            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);
            purchases.add(productMapper.toproductPurchaseResponse(product, productRequest.quantity()));
        }

        return purchases;

    }

    public List<ProductResponse> findAll() {

        var products = productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());

        return products;
    }
}
