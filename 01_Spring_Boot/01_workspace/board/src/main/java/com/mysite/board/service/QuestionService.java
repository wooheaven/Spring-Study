package com.mysite.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.board.exception.DataNotFoundException;
import com.mysite.board.model.BoardUser;
import com.mysite.board.model.Question;
import com.mysite.board.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Question> getQuestionList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createTime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

    public Question getQuestion(Long id) {
        Optional<Question> optional = this.questionRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new DataNotFoundException("Question id={" + id + "} not found");
        }
    }

    public void create(String subject, String content, BoardUser boardUser) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateTime(LocalDateTime.now());
        question.setAuthor(boardUser);
        this.questionRepository.save(question);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyTime(LocalDateTime.now());
        this.questionRepository.save(question);
    }
}
