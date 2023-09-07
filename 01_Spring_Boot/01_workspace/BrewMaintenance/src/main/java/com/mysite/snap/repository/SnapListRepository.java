package com.mysite.snap.repository;

import com.mysite.snap.entity.SnapList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnapListRepository extends JpaRepository<SnapList, Long> {
    List<SnapList> findByNameAndRev(String name, String rev);
}
