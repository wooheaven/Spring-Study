package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{

    Optional<Answer> findByAnswerId(Integer id);
}
