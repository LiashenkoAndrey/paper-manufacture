package com.paper.controllers.manufactureMachine;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.dto.CatalogWithGoodsCountDto;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import lombok.RequiredArgsConstructor;
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

    private final ManufactureMachineRepository machineRepository;

    private final ProducerRepository producerRepository;

    @GetMapping("{catalogId}")
    public String viewSpecifiedCatalog(@PathVariable("catalogId") Long catalogId,
                                       @RequestParam(value = "producerIds", required = false) String producerIds,
                                       @RequestParam(value = "priceFrom", required = false) Long priceFrom,
                                       @RequestParam(value = "priceTo", required = false) Long priceTo,
                                       Model model) throws JsonProcessingException {

        List<Catalog> catalogs = catalogRepository.findAll();
        model.addAttribute("catalogs", catalogs);

        Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(CatalogNotFoundException::new);
        model.addAttribute("catalog", catalogs.get(catalogs.indexOf(catalog)));

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

        List<ManufactureMachine> machinePage = machineRepository.findPageAndFilterBy(
                catalogId,
                producerIdsList,
                priceFrom,
                priceTo,
                PageRequest.of(0, 5)
        );

        model.addAttribute("machines", machinePage);
        model.addAttribute("totalItems", machineRepository.getTotalItems(catalogId, producerIdsList, priceFrom, priceTo));
        return "/catalog";
    }

    @GetMapping("/all")
    public @ResponseBody List<CatalogWithGoodsCountDto> getAllCatalogs() {
        return catalogRepository.findAllByType(CatalogType.MANUFACTURE_MACHINE.toString());
    }
}
