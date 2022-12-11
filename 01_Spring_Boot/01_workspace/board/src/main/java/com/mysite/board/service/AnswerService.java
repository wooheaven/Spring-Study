package com.mysite.board.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mysite.board.model.Answer;
import com.mysite.board.model.Question;
import com.mysite.board.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question, String content) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateTime(LocalDateTime.now());
        answer.setQuestion(question);
        this.answerRepository.save(answer);
    }
}
