package com.example.springshop.controllers;

import com.example.springshop.dto.BucketDTO;
import com.example.springshop.service.BucketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class BucketController {
    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping("/bucket")
    public String aboutBucket(Model model, Principal principal, HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        String JSessionId = cookies[cookies.length - 1].getValue();
        BucketDTO bucketDTO;
        if(principal == null) {
            bucketDTO = bucketService.getBucketDTOBySessionId(JSessionId);
        }
        else {
            bucketDTO = bucketService.getBucketByUser(principal.getName());
        }
        model.addAttribute("bucket", bucketDTO);
        model.addAttribute("sessionId", JSessionId);
        return "bucket";
    }

    @PostMapping("/bucket")
    public String commitBucket(Principal principal, HttpServletRequest httpServletRequest){
        if(principal != null) {
            bucketService.commitBucketToOrderByUser(principal.getName());
        }
        else {
            Cookie[] cookies = httpServletRequest.getCookies();
            String JSessionId = cookies[cookies.length - 1].getValue();
            bucketService.commitBucketToOrderBySession(JSessionId);
        }
        return "redirect:/bucket";
    }

    @GetMapping("/bucket/remove/{id}/{sessionId}")
    public String removeProduct(@PathVariable Integer id, @PathVariable String sessionId, Principal principal, Model model) {
        if(principal == null) {
            bucketService.getBucketDTOBySessionId(sessionId);
            bucketService.removeProductBySession(id, sessionId);
        } else {
            bucketService.removeProductByUser(id, principal.getName());
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDTO);
        }
        return "redirect:/bucket";
    }
}
