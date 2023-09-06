package com.mysite.snap.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "snap_list")
public class SnapList extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snap_list_id")
    private Long id;

    @Column(length = 40)
    private String name;

    @Column(length = 40)
    private String version;

    @Column(length = 20)
    private String rev;

    @Column(length = 20)
    private String tracking;

    @Column(length = 20)
    private String publisher;

    @Column(length = 20)
    private String notes;
}
