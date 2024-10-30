package com.mysite.sbb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findBySubject(String string);

}
