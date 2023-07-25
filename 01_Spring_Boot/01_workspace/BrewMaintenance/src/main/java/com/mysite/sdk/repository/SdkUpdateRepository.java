package com.mysite.sdk.repository;

import com.mysite.sdk.entity.SdkUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SdkUpdateRepository extends JpaRepository<SdkUpdate, Long> {
}
