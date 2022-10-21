package com.example.springshop.service;

import com.example.springshop.dao.ProductRepository;
import com.example.springshop.domain.Bucket;
import com.example.springshop.domain.Product;
import com.example.springshop.domain.User;
import com.example.springshop.dto.ProductDTO;
import com.example.springshop.mapper.ProductMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final BucketService bucketService;

    private final SimpMessagingTemplate template;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, BucketService bucketService, SimpMessagingTemplate template) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
        this.template = template;
    }

    @Override
    public List<ProductDTO> getAll() {
        return mapper.fromProductList(productRepository.findAll());
    }

    @Override
    public void addToUserBucket(Integer productId, String username) {
        User user = userService.findByName(username);
        if(user == null) {
            throw new RuntimeException("User not find " + username);
        }

        Bucket bucket = user.getBucket();
        if(bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
    }

    @Override
    @Transactional
    public void addProduct(ProductDTO dto) {
        Product product = mapper.toProduct(dto);
        Product savedProduct = productRepository.save(product);
        template.convertAndSend("/topic/products", ProductMapper.MAPPER.fromProduct(savedProduct));
    }
}
