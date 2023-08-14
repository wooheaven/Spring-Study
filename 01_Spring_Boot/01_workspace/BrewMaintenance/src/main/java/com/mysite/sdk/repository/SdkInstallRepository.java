package com.mysite.sdk.repository;

import com.mysite.sdk.entity.SdkInstall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SdkInstallRepository extends JpaRepository<SdkInstall, Long> {
}
