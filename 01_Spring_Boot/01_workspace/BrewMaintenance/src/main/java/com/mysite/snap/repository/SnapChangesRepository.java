package com.mysite.snap.repository;

import com.mysite.snap.entity.SnapChanges;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapChangesRepository extends JpaRepository<SnapChanges, Long> {
}
