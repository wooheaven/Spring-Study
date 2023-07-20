package com.mysite.sdk.repository;

import com.mysite.sdk.entity.SdkVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SdkVersionRepository extends JpaRepository<SdkVersion, Long> {
}
