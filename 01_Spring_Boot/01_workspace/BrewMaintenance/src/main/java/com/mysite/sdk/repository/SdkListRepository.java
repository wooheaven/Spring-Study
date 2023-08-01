package com.mysite.sdk.repository;

import com.mysite.sdk.entity.SdkList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SdkListRepository extends JpaRepository<SdkList, Long> {
}
