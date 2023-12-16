package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewDoctor;
import com.mysite.brew.entity.BrewList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrewDoctorRepository extends JpaRepository<BrewDoctor, Long> {
}
