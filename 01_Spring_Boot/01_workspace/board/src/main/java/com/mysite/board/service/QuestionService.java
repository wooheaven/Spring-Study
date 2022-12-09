package com.mysite.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.board.model.Question;
import com.mysite.board.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository qRepository;
    
    public List<Question> getQList() {
        return qRepository.findAll();
    }
}
