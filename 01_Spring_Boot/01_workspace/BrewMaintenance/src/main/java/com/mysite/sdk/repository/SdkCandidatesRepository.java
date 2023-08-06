package com.mysite.sdk.repository;

import com.mysite.sdk.entity.SdkCandidates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SdkCandidatesRepository extends JpaRepository<SdkCandidates, Long> {
}
