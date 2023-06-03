package com.mysite.brew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.brew.model.BrewLs;

public interface BrewLsRepository extends JpaRepository<BrewLs, Long>{

   List<BrewLs> findAllByPackageNameOrderById(String packageName);

}
