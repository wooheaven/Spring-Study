package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrewInfoRepository extends JpaRepository<BrewInfo, Long>{
}
