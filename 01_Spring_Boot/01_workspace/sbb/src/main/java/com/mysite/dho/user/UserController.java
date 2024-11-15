package com.mysite.dho.user;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/list")
    public String users(Model model) {
        List<User> users = this.userService.getUsers();
        model.addAttribute("users", users);
        return "users";
    }
    
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") String id) {
        User user = this.userService.getUser(id);
        model.addAttribute("user", user);
        return "user_detail";
    }
    

}
