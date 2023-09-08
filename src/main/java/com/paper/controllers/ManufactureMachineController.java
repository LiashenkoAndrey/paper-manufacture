package com.paper.controllers;

import com.paper.domain.GoodGroup;
import com.paper.domain.ManufactureMachine;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.GoodGroupRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.util.EntityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.paper.util.EntityMapper.map;

@Controller
@RequestMapping("/good/manufacture-machine")
@RequiredArgsConstructor
public class ManufactureMachineController {

    private final GoodGroupRepository goodGroupRepository;
    private final ManufactureMachineRepository repository;

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
        model.addAttribute("machines", repository.findAllByByGoodGroupId(catalogId));
        return "goods";
    }

    @GetMapping
    public String getGoodDetails(@RequestParam("id") Long id, Model model) {
        model.addAttribute("goodGroupList", goodGroupRepository.findAll());
        var machine = repository.findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
        model.addAttribute("machine", machine);
        return "good";
    }

    @GetMapping("/new")
    public String getCreationView() {
        return "manufactureMachine/new";
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody ManufactureMachine manufactureMachine) {
        if (repository.exists(Example.of(manufactureMachine))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ManufactureMachine saved = repository.save(manufactureMachine);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved.getId());
    }


    @GetMapping("/{id}/update")
    public String getUpdateView(@PathVariable("id") Long id, Model model) {
        var machine = repository.findById(id)
                .orElseThrow(ManufactureMachineNotFoundException::new);
        model.addAttribute("machine", machine);
        return "manufactureMachine/update";
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
        if (!repository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
