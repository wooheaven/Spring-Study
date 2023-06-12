package com.mysite.brew.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mysite.brew.model.BrewLs;

public interface BrewLsRepository extends JpaRepository<BrewLs, Long> {

    List<BrewLs> findAllByPackageNameOrderById(String packageName);

    BrewLs findByPackageName(String myName);

    @Query(value = ""
            + "SELECT"
            + "    brew_ls_id, "
            + "    row_number() OVER ("
            + "        ORDER BY "
            + "            CASE"
            + "                WHEN current_version IS NULL THEN 0"
            + "                ELSE 1"
            + "            END DESC,"
            + "            package ASC"
            + "    ) AS sort_number "
            + "FROM brew_ls", nativeQuery = true)
    List<Map<String, Long>> findIdSortNumberByCustom();

}
