package com.mysite.snap.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "snap_remove")
public class SnapRemove extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snap_remove_id")
    private Long id;

    @Column(length = 30)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String removeLog;
}
