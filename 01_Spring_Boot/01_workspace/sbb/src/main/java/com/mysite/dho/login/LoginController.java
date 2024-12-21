package com.mysite.dho.login;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping(value = "/login")
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final LoginService loginService;
    
    @GetMapping(value = "/list")
    public String logins(Model model) {
        List<Login> loginList = this.loginService.getLoginList();
        model.addAttribute("loginList", loginList);
        return "loginList";
    }
}
