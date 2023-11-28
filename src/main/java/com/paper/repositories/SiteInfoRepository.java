package com.paper.repositories;

import com.paper.domain.SiteInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteInfoRepository extends JpaRepository<SiteInfo, Long> {
}
