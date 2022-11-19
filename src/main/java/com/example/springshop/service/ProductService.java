package com.example.springshop.service;

import com.example.springshop.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    void addToUserBucket(Integer productId, String username, String sessionId);

    void addProduct(ProductDTO dto);

    void addProductToSession(Integer id, String JSessionId);
    ProductDTO getById(Integer id);
}
