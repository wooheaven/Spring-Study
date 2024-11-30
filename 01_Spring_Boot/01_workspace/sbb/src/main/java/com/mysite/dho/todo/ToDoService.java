package com.mysite.dho.todo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mysite.dho.user.User;
import com.mysite.sbb.question.Question;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public void create(User user, String content) {
        ToDo todo = new ToDo();
        todo.setContent(content);
        todo.setCreateDate(LocalDateTime.now());
        todo.setUser(user);
        this.toDoRepository.save(todo);
    }
}
