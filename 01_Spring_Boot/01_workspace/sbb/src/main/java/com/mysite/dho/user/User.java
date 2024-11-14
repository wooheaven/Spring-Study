package com.mysite.dho.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "DHO_USER")
public class User {
    @Id
    @Column(length = 3)
    private String userId;
    
    @Column(length = 12)
    private String gameId;

    @Column(length = 3)
    private String userNm;
}
