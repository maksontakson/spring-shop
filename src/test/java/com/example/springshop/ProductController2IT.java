package com.example.springshop;

import com.example.springshop.dto.ProductDTO;
import com.example.springshop.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DataJpaTest
class ProductController2IT {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProductService productService;

    private ProductDTO expectedProduct = new ProductDTO(99, "Test Product", BigDecimal.valueOf(999.99));

    @BeforeEach
    void setUp() {
        Mockito.when(productService.getById(Mockito.anyInt())).thenReturn(expectedProduct);
    }

    @Test
    void getById() {
        //execute
        ResponseEntity<ProductDTO> entity =
                restTemplate
                        .getForEntity("/products/" + expectedProduct.getId(), ProductDTO.class);
        //check
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());

        ProductDTO actualProduct = entity.getBody();
        Assertions.assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void addProduct() {
        //execute
        ResponseEntity<Void> response =
                restTemplate.postForEntity("/api/v1/products/", expectedProduct, Void.class);
        //check
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(productService).addProduct(Mockito.eq(expectedProduct));

        ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService).addProduct(captor.capture());

        Assertions.assertEquals(expectedProduct, captor.getValue());
    }
}
