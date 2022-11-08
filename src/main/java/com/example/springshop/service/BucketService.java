package com.example.springshop.service;

import com.example.springshop.domain.Bucket;
import com.example.springshop.domain.User;
import com.example.springshop.dto.BucketDTO;

import java.util.List;

public interface BucketService {
    Bucket getBucketBySessionId(String sessionId);
    BucketDTO getBucketDTOBySessionId(String sessionId);
    Bucket createBucket(User user, List<Integer> productIds, String sessionId);
    void addProducts(Bucket bucket, List<Integer> productIds);
    BucketDTO getBucketByUser(String name);
    void removeProductByUser(Integer id, String name);
    void removeProductBySession(Integer id, String sessionId);

    void commitBucketToOrderByUser(String name);

    void commitBucketToOrderBySession(String jSessionId);
}
