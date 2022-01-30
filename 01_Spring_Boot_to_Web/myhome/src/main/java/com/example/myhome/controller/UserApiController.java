package com.example.myhome.controller;

import com.example.myhome.model.User;
import com.example.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    List<User> all(String content) {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @PostMapping("/users")
    User add(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replace(@RequestBody User newUser, @PathVariable Long id) {
        return repository.findById(id)
            .map(user -> {
//                user.setTitle(newUser.getTitle());
//                user.setContent(newUser.getContent());
                return repository.save(user);
            })
            .orElseGet(() -> {
                newUser.setId(id);
                return repository.save(newUser);
            });
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
