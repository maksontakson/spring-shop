package com.example.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/news")
public class MyController {
    @GetMapping({"", "/"})
    public String news() {
        System.out.println("Hi!");
        return "news";
    }
}
