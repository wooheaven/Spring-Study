package com.mysite.brew.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "brew_update")
public class BrewUpdate extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_update_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
}
