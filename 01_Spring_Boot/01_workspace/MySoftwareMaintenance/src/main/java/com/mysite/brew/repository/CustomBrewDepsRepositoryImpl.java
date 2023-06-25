package com.mysite.brew.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mysite.brew.model.BrewDeps;
import com.mysite.brew.model.QBrewDeps;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBrewDepsRepositoryImpl implements CustomBrewDepsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BrewDeps> updateSortNumberByCustom(List<String> rootNodeList) {
        QBrewDeps qBrewDeps = new QBrewDeps("brew_deps");
        List<BrewDeps> brewDepsList = new ArrayList<>();
        for (String myRootNode : rootNodeList) {
            List<BrewDeps> myBrewDepsList = queryFactory
                    .selectFrom(qBrewDeps)
                    .where(qBrewDeps.rootNode.eq(myRootNode))
                    .orderBy(qBrewDeps.rootNode.asc(), qBrewDeps.level.asc(), qBrewDeps.parentNode.asc(), qBrewDeps.childNode.asc())
                    .fetch();
            brewDepsList.addAll(myBrewDepsList);
        }
        for (int i = 0; i < brewDepsList.size(); i++) {
            brewDepsList.get(i).setSortNumber((long) i);
        }
        return brewDepsList;
    }

}
