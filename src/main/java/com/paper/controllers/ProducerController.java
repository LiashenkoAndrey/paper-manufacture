package com.paper.controllers;

import com.paper.domain.Producer;
import com.paper.dto.ProducerDto;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producer")
@RequiredArgsConstructor
public class ProducerController {

    private static final Logger logger = LogManager.getLogger(ProducerController.class);

    private final ProducerService producerService;

    private final ProducerRepository producerRepository;

    @PostMapping("/new")
    public void create(@ModelAttribute Producer producer) {
        logger.info("new producer: " + producer );
        producerService.save(producer);
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
        return "/producer";
    }
}
