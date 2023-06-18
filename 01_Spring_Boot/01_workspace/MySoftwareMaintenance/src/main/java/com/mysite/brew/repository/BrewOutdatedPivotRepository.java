package com.mysite.brew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.brew.model.BrewOutdatedPivot;

public interface BrewOutdatedPivotRepository extends JpaRepository<BrewOutdatedPivot, Long> {

}
