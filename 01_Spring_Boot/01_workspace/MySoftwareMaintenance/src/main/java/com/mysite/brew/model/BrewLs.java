package com.mysite.brew.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
@Table(schema = "public", name = "brew_ls")
public class BrewLs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_ls_id")
    private Long id;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "modified_time")
    private LocalDateTime modifyTime;

    @Column(name = "package")
    private String packageName;

    @Column
    private String installed_version;

    @Column
    private String current_version;

    @Column
    private String pinned;
}
