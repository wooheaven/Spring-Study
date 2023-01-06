package com.mysite.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.board.form.BoardUserForm;
import com.mysite.board.service.BoardUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class BoardUserController {
    private final BoardUserService boardUserService;

    @GetMapping("/signup")
    public String signup(BoardUserForm boardUserForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid BoardUserForm boardUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!boardUserForm.getPassword1().equals(boardUserForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        boardUserService.create(boardUserForm.getUsername(), boardUserForm.getEmail(), boardUserForm.getPassword1());

        return "redirect:/";
    }
}
