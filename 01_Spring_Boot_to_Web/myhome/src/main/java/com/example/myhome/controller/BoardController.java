package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.repository.BoardRepository;
import com.example.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(
            Model model,
            @PageableDefault(page = 0, size = 5, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String searchText) {
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(pageable, searchText, searchText);
        int startPage = Math.max(boards.getPageable().getPageNumber() - 4, 1);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);

        model.addAttribute("boards", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("size", pageable.getPageSize());

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
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}
