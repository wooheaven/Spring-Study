package com.mysite.brew.repository;

import com.mysite.brew.entity.BrewDoctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrewDoctorRepository extends JpaRepository<BrewDoctor, Long> {

}
