package com.mysite.dho.login;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final LoginRepository loginRepository;
    
    public List<Login> getLoginList() {
        return this.loginRepository.findAll();
    }

}
