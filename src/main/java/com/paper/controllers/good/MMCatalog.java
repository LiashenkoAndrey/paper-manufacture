package com.paper.controllers.good;


import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.dto.CatalogWithGoodsCountDto;
import com.paper.repositories.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MMCatalog {

    private final CatalogRepository catalogRepository;


    @GetMapping("/good/manufacture-machine/catalog/all")
    public @ResponseBody List<CatalogWithGoodsCountDto> getAllCatalogs() {
        return catalogRepository.findAllCatalogs();
    }

    @PostMapping("/protected/catalog/new")
    private Catalog newCatalog(@RequestParam String name) {
        return catalogRepository.save( new Catalog(name));
    }
}
