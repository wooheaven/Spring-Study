package com.mysite.sdk.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "sdk_uninstall")
public class SdkUninstall extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdk_uninstall_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
}
