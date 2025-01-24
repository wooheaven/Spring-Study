package com.mysite.sdk.repository;

import com.mysite.sdk.entity.SdkVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SdkVersionRepository extends JpaRepository<SdkVersion, Long> {
    @Query(value =
"""
SELECT A.sdk_version_id, A.row_number 
  FROM ( 
SELECT s.sdk_version_id,ROW_NUMBER() OVER (ORDER BY s.sdk_version_id DESC) AS row_number
  FROM sdk_version s
 ORDER BY s.sdk_version_id DESC
) AS A
 WHERE A.row_number >= ?1
   AND A.row_number <= ?2
""", nativeQuery = true)
    List<Long> findTopTenByIdOrderByIdDesc(Long lowerRowNum, Long upperRowNum);

    @Query("SELECT s.content FROM SdkVersion s WHERE s.id = ?1")
    String findContentById(Long idI);
}
