package com.mysite.brew.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mysite.brew.model.BrewDeps;

public interface BrewDepsRepository extends JpaRepository<BrewDeps, Long>, CustomBrewDepsRepository {

    Integer countByLevel(Integer level);

    List<BrewDeps> findAllByRootNodeAndLevel(String myRootNode, Integer myLevel);

    List<BrewDeps> findAllByRootNodeAndParentNodeAndLevel(String myRootNode, String myChildNode, Integer myLevel);

    @Query(value = ""
            + "SELECT "
            + "    root_node "
            + "FROM brew_deps "
            + "GROUP BY root_node "
            + "ORDER BY root_node", nativeQuery = true)
    List<String> findGroupByRootNodeWithCustom();

    Integer countByRootNodeAndChildNode(String myRootNode, String myChildNode);

}
