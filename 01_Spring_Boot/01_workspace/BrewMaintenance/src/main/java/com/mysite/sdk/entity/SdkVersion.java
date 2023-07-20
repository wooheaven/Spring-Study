package com.mysite.sdk.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "sdk_version")
public class SdkVersion extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdk_version_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
}
