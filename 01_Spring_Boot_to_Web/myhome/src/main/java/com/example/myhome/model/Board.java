package com.example.myhome.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 3, max = 10, message = "제목의 길이는 3자 이상 10자 이하입니다.")
    private String title;
    @NotEmpty
    @Size(min = 2, max = 10)
    private String content;
}
