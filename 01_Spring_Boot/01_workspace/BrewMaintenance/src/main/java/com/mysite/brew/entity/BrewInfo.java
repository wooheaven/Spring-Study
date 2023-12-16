package com.mysite.brew.entity;

import com.mysite.common.entity.CommonLocalDateTime;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "brew_info")
public class BrewInfo extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_info_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
}
