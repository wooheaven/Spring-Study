package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewUsesInstalled;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrewUsesRepository extends JpaRepository<BrewUsesInstalled, Long> {

}
