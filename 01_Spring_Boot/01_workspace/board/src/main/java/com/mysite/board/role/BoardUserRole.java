package com.mysite.board.role;

import lombok.Getter;

@Getter
public enum BoardUserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    BoardUserRole(String value) {
        this.value = value;
    }

    private String value;
}
