package com.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("")
    public String empty() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String root() {
        return "index.html";
    }
}
