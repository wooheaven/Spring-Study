package com.mysite.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.board.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findBySubject(String q_subject);

    Optional<Question> findBySubjectAndContent(String q_subject, String q_content);

    List<Question> findBySubjectLike(String string);

    Page<Question> findAll(Pageable pageable);
}
