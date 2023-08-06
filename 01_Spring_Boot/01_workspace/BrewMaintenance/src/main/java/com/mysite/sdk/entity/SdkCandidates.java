package com.mysite.sdk.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "sdk_candidates")
public class SdkCandidates extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdk_candidates_id")
    private Long id;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private String content;
}
