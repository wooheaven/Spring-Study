package com.mysite.dho.user;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.dho.todo.ToDo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "DHO_USER")
public class User {
    @Id
    @Column(length = 3)
    private String userId;
    
    @Column(length = 12)
    private String gameId;

    @Column(length = 3)
    private String userNm;

    private LocalDateTime createDate;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ToDo> todoList;
}
