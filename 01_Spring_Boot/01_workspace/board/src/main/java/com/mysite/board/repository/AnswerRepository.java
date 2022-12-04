package com.mysite.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.board.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>{

}
