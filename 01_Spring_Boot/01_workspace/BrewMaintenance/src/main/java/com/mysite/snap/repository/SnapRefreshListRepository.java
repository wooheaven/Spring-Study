package com.mysite.snap.repository;

import com.mysite.snap.entity.SnapRefreshList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SnapRefreshListRepository extends JpaRepository<SnapRefreshList, Long> {
    @Query(value = ""
            + "  SELECT * "
            + "    FROM snap_refresh_list "
            + "   WHERE name = :name "
            + "ORDER BY snap_refresh_list_id DESC "
            + "   LIMIT 1 ", nativeQuery = true)
    SnapRefreshList findOneCustom(@Param("name") String name);
}
