package com.paper.controllers.good;


import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.dto.CatalogWithGoodsCountDto;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.repositories.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
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

    @PutMapping("/protected/catalog/{id}/update")
    public Catalog updateCatalog(@PathVariable Long id, @RequestParam String name) {
        log.info("update catalog - {}, name - {}", id, name);
        Catalog catalog = catalogRepository.findById(id).orElseThrow(CatalogNotFoundException::new);
        catalog.setName(name);
        catalogRepository.updateCatalogName(name, id);
        return catalog;
    }

    @DeleteMapping("/protected/catalog/{id}/delete")
    public Long deleteCatalog(@PathVariable Long id) {
        catalogRepository.deleteById(id);
        return id;
    }
}
