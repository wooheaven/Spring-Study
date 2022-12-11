package com.mysite.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.board.model.Question;
import com.mysite.board.service.AnswerService;
import com.mysite.board.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final AnswerService aService;
    private final QuestionService qService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id, @RequestParam String content) {
        Question question = this.qService.getQuestion(id);
        this.aService.create(question, content);
        return String.format("redirect:/question/detail/%s", id);
    }
}
