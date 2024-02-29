package com.paper.controllers.good;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paper.domain.Catalog;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.GoodPageDto;
import com.paper.dto.MMSearchDto;
import com.paper.dto.PricesWithGoodAmountsDto;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ManufactureMachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/public/good")
@RequiredArgsConstructor
@Log4j2
public class MMController {

    private final ManufactureMachineRepository repository;
    private final ManufactureMachineRepository machineRepository;
    private final CatalogRepository catalogRepository;

    @GetMapping
    public ManufactureMachine getGoodDetails(@RequestParam("id") Long id) {
        return repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
    }

    @GetMapping("/page")
    public GoodPageDto getPageOfEntities(@RequestParam(value = "catalogName", required = false) String catalogName,
                                                        @RequestParam("pageId") Integer pageId,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam(value = "priceFrom", required = false) BigDecimal priceFrom,
                                                        @RequestParam(value = "priceTo", required = false) BigDecimal priceTo) {

        log.info(catalogName);

        Catalog catalog = catalogRepository.findByName(catalogName);
        log.info(catalog);
        Page<ManufactureMachine> page = machineRepository.findAllByCatalogOrPriceBetween(catalog, priceFrom, priceTo, PageRequest.of(pageId, pageSize));
        return new GoodPageDto(page.toList(), page.getTotalElements());
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
    public GoodPageDto getAll(@RequestParam(value = "pageId",required = false, defaultValue = "0") Integer pageId,
                              @RequestParam(value = "pageSize",required = false, defaultValue = "20") Integer pageSize,
                              @RequestParam(value = "catalogName", required = false) String catalogName) {
        if (catalogName != null) {
            Page<ManufactureMachine> page = repository.getAllByCatalogName(catalogName, PageRequest.of(pageId, pageSize));
            return new GoodPageDto(page.toList(), page.getTotalElements());
        }
        Page<ManufactureMachine> page = repository.findAll( PageRequest.of(pageId, pageSize));
        return new GoodPageDto(page.toList(), page.getTotalElements());
    }
}
