package com.paper.controllers.manufactureMachine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDto;
import com.paper.dto.MMDto2;
import com.paper.dto.PricesWithGoodAmountsDto;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static com.paper.util.ControllerUtil.parseProducerIds;

@RestController
@RequestMapping("/good/manufacture-machine")
@RequiredArgsConstructor
public class MMController {

    private static final Logger logger = LogManager.getLogger(MMController.class);
    private final CatalogRepository catalogRepository;
    private final ManufactureMachineRepository repository;
    private final ManufactureMachineRepository machineRepository;


    @GetMapping
    public ManufactureMachine getGoodDetails(@RequestParam("id") Long id) {
        return repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
    }

    @GetMapping("/page")
    public @ResponseBody List<MMDto2> getPageOfEntities(@RequestParam(value = "catalogId", required = false) Long catalogId,
                                                        @RequestParam("pageId") Integer pageId,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam(value = "producerIds", required = false) List<Long> producerIds,
                                                        @RequestParam(value = "priceFrom", required = false) Long priceFrom,
                                                        @RequestParam(value = "priceTo", required = false) Long priceTo) throws JsonProcessingException {

        logger.info(producerIds);
        return machineRepository.findPageAndFilterBy(
                catalogId,
                producerIds,
                priceFrom,
                priceTo,
                Pageable.ofSize(pageSize).withPage(pageId)
        );
    }

    @GetMapping("/maxPrice")
    public ResponseEntity<?> getMaxGoodPrice() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("maxPrice", repository.getMaxGoodPrice());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(rootNode.toString());
    }

    @GetMapping("/allPricesWithAmount")
    public @ResponseBody List<PricesWithGoodAmountsDto> getAllGoodPricesWithAmount() {
        return repository.getAllPricesWithGoodAmounts();
    }

    private final Integer PAGE_SIZE = 5;


    @GetMapping("/all")
    public @ResponseBody List<MMDto> getAll(@RequestParam(value = "pageId",required = false) Integer pageId,
                                            @RequestParam(value = "pageSize",required = false) Integer pageSize) {
        if (pageId != null) {
            return repository.getAllDto(PageRequest.of(pageId, pageSize));
        } else {
            return repository.getAllDto(PageRequest.of(0, PAGE_SIZE));
        }
    }

    @GetMapping("/view/all")
    public String getPage(Model model) {
        model.addAttribute("catalogs", catalogRepository.findAll());
        model.addAttribute("machines", repository.findAll(PageRequest.of(0, PAGE_SIZE)).toList());
        return "goods";
    }

    @GetMapping("/{id}/properties")
    public @ResponseBody Map<String, String> getProperties(@PathVariable("id") Long id) {
        var manufactureMachine = repository.findById(id)
                .orElseThrow(ManufactureMachineNotFoundException::new);
        return manufactureMachine.getProperties();
    }

    @GetMapping("/serial_numbers/all")
    public @ResponseBody Map<String, Long> getAllSerialNumbers() {
        return repository.getAllSerialNumbers();
    }
}
