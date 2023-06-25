package com.mysite.brew.repository;

import java.util.List;
import java.util.Map;

import com.mysite.brew.model.BrewDeps;

public interface CustomBrewDepsRepository {

    List<BrewDeps> updateSortNumberByCustom(List<String> rootNodeList);
}
