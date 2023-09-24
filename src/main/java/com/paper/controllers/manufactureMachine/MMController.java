package com.paper.controllers.manufactureMachine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDtoInt;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static com.paper.util.ControllerUtil.parseProducerIds;

@Controller
@RequestMapping("/good/manufacture-machine")
@RequiredArgsConstructor
public class MMController {

    private static final Logger logger = LogManager.getLogger(MMController.class);
    private final CatalogRepository catalogRepository;
    private final ManufactureMachineRepository repository;

    private final ManufactureMachineService machineService;

    @PersistenceContext
    private final EntityManager manager;

    @GetMapping
    @SuppressWarnings("unchecked")
    public String getGoodDetails(@RequestParam("id") Long id, Model model) {
        model.addAttribute("catalogs", catalogRepository.findAll());
        var machine = repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
        List<String> image2 = (List<String>) manager.createQuery("select m.images from ManufactureMachine m where m.id = :goodId")
                .setParameter("goodId", id)
                .getResultList();
        model.addAttribute("images", image2);
        model.addAttribute("machine", machine);
        return "good";
    }

    @GetMapping("/page")
    public @ResponseBody List<MMDtoInt> getPageOfEntities(@RequestParam("catalogId") Long catalogId,
                                                          @RequestParam("pageId") Integer pageId,
                                                          @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam(value = "producerIds", required = false) String producerIds,
                                                          @RequestParam(value = "priceFrom", required = false) Long priceFrom,
                                                          @RequestParam(value = "priceTo", required = false) Long priceTo) throws JsonProcessingException {
        return machineService.findAllWithFilters(
                catalogId,
                priceFrom,
                priceTo,
                parseProducerIds(producerIds),
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


    @GetMapping("/all")
    public ResponseEntity<List<ManufactureMachine>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/view/all")
    public String getPage(Model model) {
        model.addAttribute("catalogs", catalogRepository.findAll());
        model.addAttribute("catalog", null);
        model.addAttribute("machines", repository.findAll());
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
