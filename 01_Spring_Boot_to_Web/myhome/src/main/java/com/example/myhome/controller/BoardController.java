package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public String boardSubmit(@Valid Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}
