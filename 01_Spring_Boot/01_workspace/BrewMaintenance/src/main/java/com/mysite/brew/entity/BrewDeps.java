package com.mysite.brew.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
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
