package com.mysite.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.board.form.QuestionForm;
import com.mysite.board.model.Question;
import com.mysite.board.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService questionService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<Question> questionList = this.questionService.getQuestionList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Question q = this.questionService.getQuestion(id);
        model.addAttribute("question", q);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        } else {
            this.questionService.create(questionForm.getSubject(), questionForm.getContent());
            return "redirect:/question/list"; // 질문 저장 후 질문목록으로 이동
        }
    }
}
