package com.mysite.brew.model;

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
@Table(schema = "public", name = "brew_deps")
public class BrewDeps extends CommonLocalDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_deps_id")
    private Long id;

    @Column
    private String rootNode;

    @Column
    private String parentNode;

    @Column
    private Integer level;

    @Column
    private String childNode;

    @Column
    private String leafNode;

    @Column
    private Long sortNumber;
}
