package com.mysite.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.board.model.Question;
import com.mysite.board.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionRepository qRepository;

    @RequestMapping("/question/list")
    public String list(Model model) {
        List<Question> qList = this.qRepository.findAll();
        model.addAttribute("qList", qList);
        return "question_list";
    }
}
