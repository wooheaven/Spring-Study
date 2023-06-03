package com.mysite.brew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.brew.model.BrewOutdated;

public interface BrewOutdatedRepository extends JpaRepository<BrewOutdated, Long>{

}
