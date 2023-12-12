package com.paper.controllers;

import com.paper.domain.SiteInfo;
import com.paper.exceptions.EntityNotFoundException;
import com.paper.repositories.SiteInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.paper.util.EntityMapper.map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class SiteInfoController {

    private final SiteInfoRepository siteInfoRepository;


    @GetMapping("/site/info")
    public SiteInfo getInfo() {
        return siteInfoRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
    }



    @PutMapping("/protected/site/info/update")
    @PreAuthorize("hasAuthority('manage:site_info')")
    public void updateInfo(@RequestBody SiteInfo siteInfo) {
        log.info(siteInfo);
        siteInfo.setId(1L);
        siteInfoRepository.save(siteInfo);
    }
}
