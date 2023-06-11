package com.mysite.brew.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mysite.brew.model.BrewLs;

public interface BrewLsRepository extends JpaRepository<BrewLs, Long> {

    List<BrewLs> findAllByPackageNameOrderById(String packageName);

    BrewLs findByPackageName(String myName);

    @Query(value = ""
            + "SELECT "
            + "    * "
            + "FROM "
            + "    public.brew_ls "
            + "ORDER BY "
            + "    CASE "
            + "        WHEN current_version IS NULL THEN 0 "
            + "        ELSE 1 "
            + "    END DESC, "
            + "    package ASC", nativeQuery = true)
    Page<BrewLs> findAllOrderByCustomWithPagination(Pageable pageable);
}
