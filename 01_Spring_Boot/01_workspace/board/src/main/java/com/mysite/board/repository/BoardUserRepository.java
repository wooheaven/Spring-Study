package com.mysite.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.board.model.BoardUser;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {

}
