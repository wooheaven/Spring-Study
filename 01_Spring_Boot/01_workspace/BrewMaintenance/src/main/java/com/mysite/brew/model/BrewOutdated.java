package com.mysite.brew.model;

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
@Table(schema = "public", name = "brew_outdated")
public class BrewOutdated extends CommonLocalDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brew_outdated_id")
    private Long id;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, String> properties = new HashMap<>();
}
