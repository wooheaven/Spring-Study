package com.mysite.brew.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "brew_outdated")
public class BrewOutdated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_outdated_id")
    private Long id;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createTime;

    @Column(name = "modified_time")
    private LocalDateTime modifyTime;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> properties = new HashMap<>();
}
