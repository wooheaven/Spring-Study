package com.mysite.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.board.model.BoardUser;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    Optional<BoardUser> findByUsername(String username);
}
