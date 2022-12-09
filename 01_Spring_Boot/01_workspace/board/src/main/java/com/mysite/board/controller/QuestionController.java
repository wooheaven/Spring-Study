package com.mysite.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.board.model.Question;
import com.mysite.board.repository.QuestionRepository;
import com.mysite.board.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService qService;

    @RequestMapping("/question/list")
    public String list(Model model) {
        List<Question> qList = this.qService.getQList();
        model.addAttribute("qList", qList);
        return "question_list";
    }
}
