package com.ms.product_service.service;
import com.ms.product_service.dto.ProductRequest;
import com.ms.product_service.dto.ProductResponse;
import com.ms.product_service.model.Product;
import com.ms.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void testCreateProduct() {
        // Given
        ProductRequest request = new ProductRequest("Laptop", "High-end gaming laptop", new BigDecimal("2500.00"));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            saved.setId("abc123"); // simulate MongoDB generated String ID
            return saved;
        });

        // When
        ProductResponse response = productService.createProduct(request);

        // Then
        verify(productRepository).save(captor.capture());
        Product savedProduct = captor.getValue();

        assertEquals("Laptop", savedProduct.getName());
        assertEquals("High-end gaming laptop", savedProduct.getDescription());
        assertEquals(new BigDecimal("2500.00"), savedProduct.getPrice());

        assertEquals("abc123", response.id());
        assertEquals("Laptop", response.name());
        assertEquals("High-end gaming laptop", response.description());
        assertEquals(new BigDecimal("2500.00"), response.price());
    }

    @Test
    void testGetAllProducts() {
        // Given
        List<Product> productList = List.of(
                new Product("abc123", "Phone", "Smartphone", new BigDecimal("999.99")),
                new Product("xyz789", "Tablet", "Android Tablet", new BigDecimal("499.99"))
        );
        when(productRepository.findAll()).thenReturn(productList);

        // When
        List<ProductResponse> responses = productService.getAllProducts();

        // Then
        verify(productRepository).findAll();
        assertEquals(2, responses.size());

        ProductResponse first = responses.get(0);
        assertEquals("abc123", first.id());
        assertEquals("Phone", first.name());
        assertEquals("Smartphone", first.description());
        assertEquals(new BigDecimal("999.99"), first.price());

        ProductResponse second = responses.get(1);
        assertEquals("xyz789", second.id());
        assertEquals("Tablet", second.name());
        assertEquals("Android Tablet", second.description());
        assertEquals(new BigDecimal("499.99"), second.price());
    }
}
