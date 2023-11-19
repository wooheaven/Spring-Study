package com.mysite.brew.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "brew_doctor")
public class BrewDoctor extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_doctor_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
}
