package com.paper.controllers.manufactureMachine;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.domain.Producer;
import com.paper.dto.MMDto2;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import com.paper.services.CatalogService;
import com.paper.services.ManufactureMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


import static com.paper.util.ControllerUtil.parseProducerIds;

@Controller
@RequestMapping("/good/manufacture-machine/catalog")
@RequiredArgsConstructor
public class MMCatalog {

    private final CatalogRepository catalogRepository;
    private final ManufactureMachineService machineService;

    private final ManufactureMachineRepository machineRepository;

    private final ProducerRepository producerRepository;

    private final CatalogService catalogService;

    @GetMapping("{catalogId}")
    public String viewSpecifiedCatalog(@PathVariable("catalogId") Long catalogId,
                                       @RequestParam(value = "producerIds", required = false) String producerIds,
                                       @RequestParam(value = "priceFrom", required = false) Long priceFrom,
                                       @RequestParam(value = "priceTo", required = false) Long priceTo,
                                       Model model) throws JsonProcessingException {

        List<Catalog> translatedCatalogs = catalogService.translateAll(catalogRepository.findAll());
        model.addAttribute("catalogs", translatedCatalogs);

        Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(CatalogNotFoundException::new);
        model.addAttribute("catalog", translatedCatalogs.get(translatedCatalogs.indexOf(catalog)));

        List<Producer> producers = producerRepository.findAll();
        model.addAttribute("producers", producers);

        List<Long> producerIdsList = parseProducerIds(producerIds);
        model.addAttribute("priceTo", priceTo);
        model.addAttribute("priceFrom", priceFrom);

        List<Producer> selected = producers.stream()
                .filter(producer -> producerIdsList.contains(producer.getId()))
                .toList();

        model.addAttribute("selectedProducersIdsAndNames", selected.stream()
                .collect(Collectors.toMap(Producer::getId, Producer::getName)));

        model.addAttribute("selectedProducersIds", selected.stream()
                .map(Producer::getId)
                .toList());

        Page<MMDto2> machinePage = machineRepository.findPageAndFilterBy(
                catalogId,
                producerIdsList,
                priceFrom,
                priceTo,
                PageRequest.of(0, 5)
        );

        System.out.println(machinePage.toList());
        model.addAttribute("machines", machineService.translateAllNamesDto(machinePage.toList()));
        model.addAttribute("totalItems", machinePage.getTotalElements());
        return "/catalog";
    }

    @GetMapping("/all")
    public @ResponseBody List<Catalog> getAllCatalogs() {
        return catalogRepository.findAllByType(CatalogType.MANUFACTURE_MACHINE);
    }
}
