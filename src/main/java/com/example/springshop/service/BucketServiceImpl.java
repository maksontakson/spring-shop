package com.example.springshop.service;

import com.example.springshop.dao.BucketRepository;
import com.example.springshop.dao.ProductRepository;
import com.example.springshop.domain.*;
import com.example.springshop.dto.BucketDTO;
import com.example.springshop.dto.BucketDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private final OrderService orderService;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserService userService, OrderService orderService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public Bucket createBucket(User user, List<Integer> productIds, String sessionId) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsByIds(productIds);
        bucket.setProducts(productList);
        bucket.setSessionId(sessionId);
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
        if(user == null || user.getBucket() == null) {
            return new BucketDTO();
        }

        BucketDTO bucketDTO = new BucketDTO();
        Map<Integer, BucketDetailDTO> mapByProductId = new HashMap<>();
        List<Product> products = user.getBucket().getProducts();

        return updateAndGetBucketDTO(bucketDTO, mapByProductId, products);
    }

    private BucketDTO updateAndGetBucketDTO(BucketDTO bucketDTO, Map<Integer, BucketDetailDTO> mapByProductId, List<Product> products) {
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
    public Bucket getBucketBySessionId(String sessionId) {
        Bucket bucket = bucketRepository.getBucketBySessionId(sessionId);
        return bucket;
    }

    @Override
    public BucketDTO getBucketDTOBySessionId(String sessionId) {
        Bucket bucket = getBucketBySessionId(sessionId);
        if(bucket == null) {
            return new BucketDTO();
        }
        BucketDTO bucketDTO = new BucketDTO();
        Map<Integer, BucketDetailDTO> mapByProductId = new HashMap<>();
        List<Product>products = bucket.getProducts();
        return updateAndGetBucketDTO(bucketDTO, mapByProductId, products);
    }

    @Override
    public void removeProductByUser(Integer id, String name) {
        User user = userService.findByName(name);
        Bucket bucket = user.getBucket();
        List<Product> products = bucket.getProducts();
        products.remove(productRepository.getReferenceById(id));
        bucket.setProducts(products);
        bucketRepository.save(bucket);
    }

    @Override
    public void removeProductBySession(Integer id, String sessionId) {
        Bucket bucket = getBucketBySessionId(sessionId);
        List<Product> products = bucket.getProducts();
        products.remove(productRepository.getReferenceById(id));
        bucket.setProducts(products);
        bucketRepository.save(bucket);
    }

    @Override
    public void commitBucketToOrderByUser(String name) {
        User user = userService.findByName(name);
        if(user == null){
            throw new RuntimeException("User is not found");
        }
        Bucket bucket = user.getBucket();
        if(bucket == null || bucket.getProducts().isEmpty()){
            return;
        }
        System.out.println("After if statements");
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Product, Long> productWithAmount = bucket.getProducts().stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));

        List<OrderDetails> orderDetails = productWithAmount.entrySet().stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());

        BigDecimal total = new BigDecimal(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());

        order.setDetails(orderDetails);
        order.setSum(total);
        order.setAddress("none");

        orderService.save(order);
        bucket.getProducts().clear();
        bucketRepository.save(bucket);
    }

    @Override
    public void commitBucketToOrderBySession(String jSessionId) {
        Bucket bucket = getBucketBySessionId(jSessionId);
        if(bucket == null || bucket.getProducts().isEmpty()) {
            return;
        }
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(null);

        Map<Product, Long> productWithAmount = bucket.getProducts().stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));

        List<OrderDetails> orderDetails = productWithAmount.entrySet().stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());

        BigDecimal total = new BigDecimal(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());

        order.setDetails(orderDetails);
        order.setSum(total);
        order.setAddress("none");

        orderService.save(order);
        bucket.getProducts().clear();
        bucketRepository.save(bucket);
    }
}
