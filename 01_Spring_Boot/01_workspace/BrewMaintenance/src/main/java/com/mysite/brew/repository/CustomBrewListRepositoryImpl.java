package com.mysite.brew.repository;

import com.mysite.brew.entity.QBrewList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBrewListRepositoryImpl implements CustomBrewListRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long customFindSecondIDByLastName(String lastName) {
        QBrewList qBrewList = new QBrewList("brew_list");
        return queryFactory
                .select(qBrewList.id)
                .from(qBrewList)
                .where(qBrewList.name.eq(lastName))
                .orderBy(qBrewList.id.desc())
                .offset(1)
                .limit(1)
                .fetchOne();
    }
}
