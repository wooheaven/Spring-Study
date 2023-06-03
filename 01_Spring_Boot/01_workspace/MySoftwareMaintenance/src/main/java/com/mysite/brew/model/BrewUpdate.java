package com.mysite.brew.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "brew_update")
public class BrewUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_update_id")
    private Long id;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createTime;

    @Column(name = "modified_time")
    private LocalDateTime modifyTime;

    @Column(columnDefinition = "TEXT")
    private String content;
}
