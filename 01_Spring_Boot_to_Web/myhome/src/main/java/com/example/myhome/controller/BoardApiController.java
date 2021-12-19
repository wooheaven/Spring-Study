package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardApiController {
    @Autowired
    private BoardRepository repository;

    @GetMapping("/boards")
    List<Board> all(@RequestParam (required = false) String title, @RequestParam (required = false) String content) {
        if (null == title && null == content) {
            return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        } else {
            return repository.findAllByTitleContainsAndContentContainsOrderByIdAsc(title, content);
        }
    }

    @PostMapping("/boards")
    Board add(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replace(@RequestBody Board newBoard, @PathVariable Long id) {
        return repository.findById(id)
            .map(board -> {
                board.setTitle(newBoard.getTitle());
                board.setContent(newBoard.getContent());
                return repository.save(board);
            })
            .orElseGet(() -> {
                newBoard.setId(id);
                return repository.save(newBoard);
            });
    }

    @DeleteMapping("/boards/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
