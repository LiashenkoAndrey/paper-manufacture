package com.paper.controllers.manufactureMachine;


import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ManufactureMachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/good/manufacture-machine/catalog")
@RequiredArgsConstructor
public class MMCatalog {

    private final CatalogRepository catalogRepository;

    private final ManufactureMachineRepository repository;


    @GetMapping("{catalogId}")
    public String viewSpecifiedCatalog(@PathVariable("catalogId") Long catalogId, Model model) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(CatalogNotFoundException::new);

        model.addAttribute("catalogs", catalogRepository.findAll());
        model.addAttribute("catalog", catalog);
        model.addAttribute("machines", repository.findAllByCatalogId(catalogId));
        return "goods";
    }

    @GetMapping("/all")
    public @ResponseBody List<Catalog> getAllCatalogs() {
        return catalogRepository.findAllByType(CatalogType.MANUFACTURE_MACHINE);
    }
}
