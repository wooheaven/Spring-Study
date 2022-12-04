package com.mysite.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping("/board")
    @ResponseBody
    public String index() {
        return "hello welcome to board";
    }
}
