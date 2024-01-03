package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewDeps;
import com.mysite.brew.entity.QBrewDeps;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CustomBrewDepsRepositoryImpl implements CustomBrewDepsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BrewDeps> customUpdateSortNumber(List<String> rootNodeList) {
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
