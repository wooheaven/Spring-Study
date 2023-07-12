package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewOutdated;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrewOutdatedRepository extends JpaRepository<BrewOutdated, Long>{

    BrewOutdated findFirstByOrderByIdDesc();

}
