package com.mysite.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysite.board.model.BoardUser;
import com.mysite.board.repository.BoardUserRepository;
import com.mysite.board.role.BoardUserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardUserSecurityService implements UserDetailsService {
    private final BoardUserRepository boardUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BoardUser> optional = this.boardUserRepository.findByUsername(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        BoardUser boardUser = optional.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(BoardUserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(BoardUserRole.USER.getValue()));
        }
        return new User(boardUser.getUsername(), boardUser.getPassword(), authorities);
    }
}
