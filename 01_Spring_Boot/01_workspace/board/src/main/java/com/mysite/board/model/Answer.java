package com.mysite.board.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long id;

    @Column(columnDefinition = "TEXT", name = "a_content")
    private String content;

    @CreatedDate
    private LocalDateTime createTime;

    @ManyToOne
    private Question question;

    @ManyToOne
    private BoardUser author;

    private LocalDateTime modifyTime;
}
