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
