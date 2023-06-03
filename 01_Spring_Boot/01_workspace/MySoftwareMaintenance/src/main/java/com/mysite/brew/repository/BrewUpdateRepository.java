package com.mysite.brew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.brew.model.BrewUpdate;

public interface BrewUpdateRepository extends JpaRepository<BrewUpdate, Long>{

}
