package com.mysite.brew.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "brew_outdated_pivot")
public class BrewOutdatedPivot extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_outdated_pivot_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column
    private String installedVersion;

    @Column
    private String currentVersion;

    @Column
    private boolean pinned;

    @Column
    private Long sortNumber;
}
