package com.mysite.sdk.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "sdk_list")
public class SdkList extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdk_list_id")
    private Long id;

    @Column(length = 20)
    private String lib;

    @Column(length = 20)
    private String vendor;

    @Column(length = 20)
    private String use;

    @Column(length = 20)
    private String version;

    @Column(length = 20)
    private String dist;

    @Column(length = 20)
    private String status;

    @Column(length = 20)
    private String identifier;
}
