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

    @Column(columnDefinition = "TEXT")
    private String content;
}
