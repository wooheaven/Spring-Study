package com.mysite.board.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.board.form.AnswerForm;
import com.mysite.board.model.BoardUser;
import com.mysite.board.model.Question;
import com.mysite.board.service.AnswerService;
import com.mysite.board.service.BoardUserService;
import com.mysite.board.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final BoardUserService boardUserService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id, @Valid AnswerForm answerForm,
            BindingResult bindingResult, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        BoardUser boardUser = this.boardUserService.getBoardUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        this.answerService.create(question, answerForm.getContent(), boardUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
