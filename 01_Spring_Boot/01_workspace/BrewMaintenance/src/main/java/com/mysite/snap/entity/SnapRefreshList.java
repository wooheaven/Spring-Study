package com.mysite.snap.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "snap_refresh_list")
public class SnapRefreshList extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snap_refresh_list_id")
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String version;

    @Column(length = 20)
    private String rev;

    @Column(length = 20)
    private String size;

    @Column(length = 20)
    private String publisher;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String refreshLog;
}
