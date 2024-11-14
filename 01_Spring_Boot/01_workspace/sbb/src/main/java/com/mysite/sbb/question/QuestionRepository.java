package com.mysite.sbb.question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findBySubject(String string);

    Optional<Question> findBySubjectAndContent(String subject, String content);

    Optional<Question> findFirstByOrderByIdDesc();

    List<Question> findBySubjectLike(String subject);

}
