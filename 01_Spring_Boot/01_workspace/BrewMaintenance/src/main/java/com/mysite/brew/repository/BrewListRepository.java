package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrewListRepository extends JpaRepository<BrewList, Long>, CustomBrewListRepository {

    @Query(value = "SELECT * FROM brew_list WHERE :secondID < brew_list_id ORDER BY brew_list_id", nativeQuery = true)
    Page<BrewList> findAllByLast(Long secondID, Pageable pageable);

    @Query(value = "SELECT name FROM brew_list ORDER BY brew_list_id DESC LIMIT 1", nativeQuery = true)
    String findLastName();
}
