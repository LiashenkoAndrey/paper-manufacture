package com.paper.controllers;

import com.paper.domain.SiteInfo;
import com.paper.exceptions.EntityNotFoundException;
import com.paper.exceptions.ValidationException;
import com.paper.repositories.SiteInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/site/info")
public class SiteInfoController {

    private final SiteInfoRepository siteInfoRepository;

    @GetMapping
    public SiteInfo getInfo() {
        return siteInfoRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping("/update")
    public void updateInfo(@ModelAttribute SiteInfo siteInfo) {
        if (siteInfo.getLogoId().isEmpty() || siteInfo.getTitle().isEmpty()) {
            throw new ValidationException("One of the fields is not valid!");
        }

        siteInfoRepository.save(siteInfo);
    }
}
