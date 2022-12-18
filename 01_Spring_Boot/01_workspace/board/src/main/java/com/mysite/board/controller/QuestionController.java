package com.mysite.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.board.model.Question;
import com.mysite.board.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService qService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<Question> questionList = this.qService.getQuestionList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Question q = this.qService.getQuestion(id);
        model.addAttribute("question", q);
        return "question_detail";
    }
}
