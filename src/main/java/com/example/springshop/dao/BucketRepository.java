package com.example.springshop.dao;

import com.example.springshop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Integer> {
    Bucket getBucketBySessionId(String sessionId);
}
