package com.mysite.board.service;

import java.time.LocalDateTime;
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

    private final QuestionRepository questionRepository;

    public List<Question> getQuestionList() {
        return questionRepository.findAll();
    }

    public Question getQuestion(Long id) {
        Optional<Question> optional = this.questionRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new DataNotFoundException("Question id={" + id + "} not found");
        }
    }

    public void create(String subject, String content) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateTime(LocalDateTime.now());
        this.questionRepository.save(question);
    }
}
