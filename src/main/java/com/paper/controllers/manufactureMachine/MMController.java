package com.paper.controllers.manufactureMachine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMSearchDto;
import com.paper.dto.PricesWithGoodAmountsDto;
import com.paper.dto.SerialNumberDto;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.ManufactureMachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/good/manufacture-machine")
@RequiredArgsConstructor
public class MMController {

    private final ManufactureMachineRepository repository;
    private final ManufactureMachineRepository machineRepository;

    @GetMapping
    public ManufactureMachine getGoodDetails(@RequestParam("id") Long id) {
        return repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
    }

    @GetMapping("/page")
    public List<ManufactureMachine> getPageOfEntities(@RequestParam(value = "catalogName", required = false) String catalogName,
                                                        @RequestParam("pageId") Integer pageId,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam(value = "producerIds", required = false) List<Long> producerIds,
                                                        @RequestParam(value = "priceFrom", required = false) Long priceFrom,
                                                        @RequestParam(value = "priceTo", required = false) Long priceTo) {

        Long[] price = getPrice(priceFrom, priceTo);
        return machineRepository.findPageAndFilterBy(
                catalogName,
                producerIds,
                price[0],
                price[1],
                Pageable.ofSize(pageSize).withPage(pageId)
        );
    }


    private Long[] getPrice(Long from, Long to) {
        if (from == 0 && to == 0) return new Long[]{null ,null};
        else return new Long[]{from ,to};
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

    @GetMapping("/search")
    public List<MMSearchDto> search(@RequestParam("q") String query) {
        return repository.findByNameContainingOrSerialNumberContainingIgnoreCase(query);
    }


    @GetMapping("/all")
    public List<ManufactureMachine> getAll(@RequestParam(value = "pageId",required = false) Integer pageId,
                                             @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                           @RequestParam(value = "catalogName", required = false) String catalogName) {
        if (catalogName != null) {
          return repository.getAllByCatalogName(catalogName);
        }
        return repository.getAll(Pageable.unpaged());
    }


    @GetMapping("/{id}/properties")
    public @ResponseBody Map<String, String> getProperties(@PathVariable("id") Long id) {
        var manufactureMachine = repository.findById(id)
                .orElseThrow(ManufactureMachineNotFoundException::new);
        return manufactureMachine.getProperties();
    }

    @GetMapping("/serial_numbers/all")
    public List<SerialNumberDto> getAllSerialNumbers() {
        return repository.getAllSerialNumbers();
    }
}
