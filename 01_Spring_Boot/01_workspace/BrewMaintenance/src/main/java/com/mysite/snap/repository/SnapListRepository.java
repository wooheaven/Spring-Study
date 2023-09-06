package com.mysite.snap.repository;

import com.mysite.snap.entity.SnapList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapListRepository extends JpaRepository<SnapList, Long> {
}
