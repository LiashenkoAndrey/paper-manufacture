package com.paper.controllers;

import com.paper.domain.Producer;
import com.paper.dto.ProducerDto;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/producer")
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;

    private final ProducerRepository producerRepository;

    @PostMapping("/new")
    public ResponseEntity<?> create(@Valid @RequestBody ProducerDto producerDto) {
        Producer saved = producerService.save(producerDto);
        System.out.println(producerDto.getImage());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved.getId());
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Producer>> getAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(producerRepository.findAll());
    }

    @GetMapping("/{id}")
    public String getInfo(@PathVariable("id") Long id, Model model) {
        var producer = producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);
        model.addAttribute("producer", producer);
        return "producer";
    }
}
