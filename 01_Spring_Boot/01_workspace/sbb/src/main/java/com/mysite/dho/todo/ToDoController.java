package com.mysite.dho.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.dho.user.User;
import com.mysite.dho.user.UserService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequestMapping(value = "/todo")
@RequiredArgsConstructor
@Controller
public class ToDoController {

    private final UserService userService;
    private final ToDoService toDoService;
    
    @PostMapping(value = "/create/{id}")
    public String createToDo(Model model, @PathVariable("id") String id, @RequestParam(value = "content") String content) {
        User user = this.userService.getUser(id);
        this.toDoService.create(user, content);
        return String.format("redirect:/user/detail/%s", id);
    }
}
