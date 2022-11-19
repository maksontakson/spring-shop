package com.example.springshop.dao;

import com.example.springshop.domain.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;

public interface BucketRepository extends JpaRepository<Bucket, Integer> {
    Bucket getBucketBySessionId(String sessionId);
}
