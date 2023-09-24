package com.mysite.snap.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "snap_changes")
public class SnapChanges extends CommonLocalDateTime {
    @Id
    @Column(name = "snap_changes_id")
    private Long id;

    @Column(length = 20)
    private String status;

    @Column(length = 30)
    private String spawn;

    @Column(length = 30)
    private String ready;

    @Column(columnDefinition = "TEXT")
    private String summary;
}
