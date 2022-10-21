package com.example.springshop.service;

import com.example.springshop.domain.Bucket;
import com.example.springshop.domain.Product;
import com.example.springshop.domain.User;
import com.example.springshop.dto.BucketDTO;

import java.util.List;

public interface BucketService {
    Bucket createBucket(User user, List<Integer> productIds);
    void addProducts(Bucket bucket, List<Integer> productIds);
    BucketDTO getBucketByUser(String name);
    void removeProduct(Integer id, String name);
}
