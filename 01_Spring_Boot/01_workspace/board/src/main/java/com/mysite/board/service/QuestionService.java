package com.mysite.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.board.exception.DataNotFoundException;
import com.mysite.board.model.Question;
import com.mysite.board.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository qRepository;

    public List<Question> getQuestionList() {
        return qRepository.findAll();
    }

    public Question getQuestion(Long id) {
        Optional<Question> optional = this.qRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new DataNotFoundException("Question id={" + id + "} not found");
        }
    }
}
