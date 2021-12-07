package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.repository.BoardRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boards = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String boardForm(Model model, @RequestParam(required = false) Long id) {
        if (null == id) {
            model.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.getById(id);
            model.addAttribute("board", board);
        }
        return "board/form";
    }

    @PostMapping("/form")
    public String boardSubmit(@ModelAttribute Board board) {
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}
