package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewDeps;

import java.util.List;

public interface CustomBrewDepsRepository {

    List<BrewDeps> updateSortNumberByCustom(List<String> rootNodeList);
}
