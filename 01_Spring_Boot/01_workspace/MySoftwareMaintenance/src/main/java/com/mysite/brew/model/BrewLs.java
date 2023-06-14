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
@Table(schema = "public", name = "brew_ls")
public class BrewLs extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_ls_id")
    private Long id;

    @Column(name = "package")
    private String packageName;

    @Column
    private String installedVersion;

    @Column
    private String currentVersion;

    @Column
    private String pinned;

    @Column
    private Long sortNumber;
}
