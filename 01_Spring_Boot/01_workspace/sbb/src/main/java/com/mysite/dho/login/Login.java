package com.mysite.dho.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "DHO_LOGIN")
public class Login {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @Column(length = 3)
    private String leftUserId;
    
    @Column(length = 12)
    private String leftGameId;
    
    @Column(length = 3)
    private String leftUserNm;

    @Column(length = 3)
    private String rightUserId;

    @Column(length = 12)
    private String rightGameId;
    
    @Column(length = 3)
    private String rightUserNm;
}
