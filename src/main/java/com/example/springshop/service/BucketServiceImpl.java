package com.example.springshop.service;

import com.example.springshop.dao.BucketRepository;
import com.example.springshop.dao.ProductRepository;
import com.example.springshop.domain.Bucket;
import com.example.springshop.domain.Product;
import com.example.springshop.domain.User;
import com.example.springshop.dto.BucketDTO;
import com.example.springshop.dto.BucketDetailDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public Bucket createBucket(User user, List<Integer> productIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsByIds(productIds);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Integer> productIds) {
        return productIds.stream().map(productRepository::getReferenceById).collect(Collectors.toList());
    }

    @Override
    public void addProducts(Bucket bucket, List<Integer> productIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productIds));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO getBucketByUser(String name) {
        User user = userService.findByName(name);
        if(user == null && user.getBucket() == null) return new BucketDTO();

        BucketDTO bucketDTO = new BucketDTO();
        Map<Integer, BucketDetailDTO> mapByProductId = new HashMap<>();
        List<Product> products = user.getBucket().getProducts();
        for(Product product : products) {
            BucketDetailDTO bucketDetailDTO = mapByProductId.get(product.getId());
            if(bucketDetailDTO == null) {
                mapByProductId.put(product.getId(), new BucketDetailDTO(product));
            }
            else {
                bucketDetailDTO.setAmount(bucketDetailDTO.getAmount().add(new BigDecimal(1.0)));
                bucketDetailDTO.setSum(bucketDetailDTO.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }

        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }

    @Override
    public void removeProduct(Integer id, String name) {
        User user = userService.findByName(name);
        Bucket bucket = user.getBucket();
        List<Product> products = bucket.getProducts();
        products.remove(productRepository.getReferenceById(id));
        bucket.setProducts(products);
        bucketRepository.save(bucket);
    }
}
