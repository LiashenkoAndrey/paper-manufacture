package com.paper.controllers;

import com.paper.domain.ManufactureMachine;
import com.paper.repositories.GoodGroupRepository;
import com.paper.repositories.ManufactureMachineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/good/manufacture-machine")
@RequiredArgsConstructor
public class GoodController {

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

    @GetMapping
    public String getGoodDetails(@RequestParam("id") Long id, Model model) {
        model.addAttribute("goodGroupList", goodGroupRepository.findAll());
        var machine = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("machine", machine);
        return "good";
    }
}
