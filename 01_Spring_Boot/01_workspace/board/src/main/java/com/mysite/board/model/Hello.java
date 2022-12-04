package com.mysite.board.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Hello {
    private final long id;
    private final String content;
}
