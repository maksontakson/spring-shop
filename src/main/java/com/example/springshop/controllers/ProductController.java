package com.example.springshop.controllers;

import com.example.springshop.dto.ProductDTO;
import com.example.springshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        List<ProductDTO> list = productService.getAll();
        model.addAttribute("products", list);
        return "products";
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Integer id, Principal principal, HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String JSessionId = cookies[cookies.length - 1].getValue();
        if(principal == null) {
            productService.addProductToSession(id, JSessionId);
            return "redirect:/products";
        } else {
            productService.addToUserBucket(id, principal.getName(), JSessionId);
        }
        return "redirect:/products";
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(ProductDTO dto) {
        productService.addProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @MessageMapping("/products")
    public void messageAddProduct(ProductDTO dto) {
        productService.addProduct(dto);
    }
}
