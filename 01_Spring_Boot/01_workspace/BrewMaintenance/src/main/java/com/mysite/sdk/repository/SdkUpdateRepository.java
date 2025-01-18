package com.mysite.sdk.repository;

import com.mysite.sdk.entity.SdkUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SdkUpdateRepository extends JpaRepository<SdkUpdate, Long> {
    @Query(value =
"""
SELECT A.sdk_update_id, A.row_number 
  FROM ( 
SELECT s.sdk_update_id,ROW_NUMBER() OVER (ORDER BY s.sdk_update_id DESC) AS row_number
  FROM sdk_update s
 ORDER BY s.sdk_update_id DESC
) AS A
 WHERE A.row_number >= ?1
   AND A.row_number <= ?2
""", nativeQuery = true)
    List<Long> findTopTenByIdOrderByIdDesc(Long lowerRowNumber, Long upperRowNumber);

    @Query("SELECT s.content FROM SdkUpdate s WHERE s.id = ?1")
    String findContentById(Long id);
}
