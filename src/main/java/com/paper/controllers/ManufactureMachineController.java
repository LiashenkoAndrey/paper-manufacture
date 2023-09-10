package com.paper.controllers;

import com.paper.domain.GoodGroup;
import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.ManufactureMachineDto;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.GoodGroupRepository;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import jakarta.persistence.EntityNotFoundException;
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

import java.io.IOException;
import java.util.*;

import static com.paper.util.EntityMapper.map;

@Controller
@RequestMapping("/good/manufacture-machine")
@RequiredArgsConstructor
public class ManufactureMachineController {

    private final GoodGroupRepository goodGroupRepository;
    private final ManufactureMachineRepository repository;

    private final ImageRepository imageRepository;

    private final ManufactureMachineService machineService;

    @GetMapping("/all")
    public ResponseEntity<List<ManufactureMachine>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/view/all")
    public String getPage(Model model) {
        model.addAttribute("goodGroupList", goodGroupRepository.findAll());
        model.addAttribute("machines", repository.findAll());
        return "goods";
    }

    @GetMapping("/catalog/{catalogId}/view")
    public String viewSpecifiedCatalog(@PathVariable("catalogId") Long catalogId, Model model) {
        GoodGroup goodGroup = goodGroupRepository.findById(catalogId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println(goodGroup);
        model.addAttribute("goodGroupList", goodGroupRepository.findAll());
        model.addAttribute("goodGroup", goodGroup);
        model.addAttribute("machines", repository.findAllByGoodGroupId(catalogId));
        return "goods";
    }

    @GetMapping
    public String getGoodDetails(@RequestParam("id") Long id, Model model) {
        model.addAttribute("goodGroupList", goodGroupRepository.findAll());
        var machine = repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
        model.addAttribute("machine", machine);
        return "good";
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody ManufactureMachineDto dto){
        var manufactureMachine = new ManufactureMachine(dto.getDescription(), dto.getName(), dto.getProperties());

        if (dto.getImages().size() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It needs at least a one image to save a good");
        } else if (repository.exists(Example.of(manufactureMachine))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ManufactureMachine saved = machineService.save(manufactureMachine, dto.getImages());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved.getId());
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ManufactureMachine manufactureMachine) {
        if (repository.exists(Example.of(manufactureMachine))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ManufactureMachine saved = repository.findById(id)
                .orElseThrow(ManufactureMachineNotFoundException::new);

        map(manufactureMachine, saved)
                .mapEmptyString(false)
                .mapNull(false)
                .map();

        repository.save(saved);
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
}
