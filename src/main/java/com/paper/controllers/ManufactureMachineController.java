package com.paper.controllers;

import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Controller
@RequestMapping("/good/manufacture-machine")
@RequiredArgsConstructor
public class ManufactureMachineController {

    private final CatalogRepository catalogRepository;
    private final ManufactureMachineRepository repository;

    private final ImageRepository imageRepository;

    private final ManufactureMachineService machineService;

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

    @GetMapping("/catalog/{catalogId}")
    public String viewSpecifiedCatalog(@PathVariable("catalogId") Long catalogId, Model model) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(CatalogNotFoundException::new);

        model.addAttribute("catalogs", catalogRepository.findAll());
        model.addAttribute("catalog", catalog);
        model.addAttribute("machines", repository.findAllByCatalogId(catalogId));
        return "goods";
    }

    @GetMapping("/{id}/properties")
    public @ResponseBody Map<String, String> getProperties(@PathVariable("id") Long id) {
        var manufactureMachine = repository.findById(id)
                .orElseThrow(ManufactureMachineNotFoundException::new);
        return manufactureMachine.getProperties();
    }

    @GetMapping("/catalog/all")
    public @ResponseBody List<Catalog> getAllCatalogs() {
        return catalogRepository.findAllByType(CatalogType.MANUFACTURE_MACHINE);
    }

    @GetMapping
    public String getGoodDetails(@RequestParam("id") Long id, Model model) {
        model.addAttribute("catalogs", catalogRepository.findAll());
        var machine = repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
        model.addAttribute("machine", machine);
        return "good";
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody ManufactureMachineDto dto) {
        if (dto.getImages().size() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It needs at least a one image to save a good");
        } else if (repository.exists(Example.of(dto.getManufactureMachine()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ManufactureMachine saved = machineService.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved.getId());
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ManufactureMachineDto dto) {
        ManufactureMachine saved = repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
        machineService.update(saved, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        var manufactureMachine = repository
                .findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
        if (!repository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        } else {
            repository.deleteById(id);
            for (String image : manufactureMachine.getImages()) {
                imageRepository.deleteById(image);
            }
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/serial_numbers/all")
    public @ResponseBody Map<String, Long> getAllSerialNumbers() {
        return repository.getAllSerialNumbers();
    }
}
