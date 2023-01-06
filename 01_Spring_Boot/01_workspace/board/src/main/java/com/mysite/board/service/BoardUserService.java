package com.mysite.board.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.board.model.BoardUser;
import com.mysite.board.repository.BoardUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardUserService {

    private final BoardUserRepository boardUserRepository;
    private final PasswordEncoder passwordEncoder;

    public BoardUser create(String username, String email, String password) {
        BoardUser boardUser = new BoardUser();
        boardUser.setUsername(username);
        boardUser.setEmail(email);
        boardUser.setPassword(passwordEncoder.encode(password));
        this.boardUserRepository.save(boardUser);
        return boardUser;
    }
}
