package com.example.myhome.repository;

import com.example.myhome.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByTitleContainsAndContentContainsOrderByIdAsc(String title, String content);
}
