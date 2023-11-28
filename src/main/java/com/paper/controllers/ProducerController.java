package com.paper.controllers;

import com.paper.domain.Producer;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ProducerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        logger.info("New producer: " + producer );
        producerService.save(producer);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Producer>> getAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(producerRepository.findAll());
    }

    @GetMapping("/{id}")
    public Producer getInfo(@PathVariable("id") Long id) {
        return producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);
    }

    @PutMapping("/update")
    private void update(@RequestParam("name") String name,
                        @RequestParam("id") Long id,
                        @RequestParam("description") String desc,
                        @RequestParam("websiteUrl") String website) {
        logger.info("Edit a producer: ", name, id, desc, website);
        Producer producer = producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);

        producer.setDescription(desc);
        producer.setName(name);
        producer.setWebsiteUrl(website);

        producerRepository.save(producer);
    }

    @DeleteMapping("/delete")
    private void delete(@RequestParam("id") Long id) {
        if (producerRepository.existsById(id)) {
            producerRepository.deleteById(id);
        } else throw new ProducerNotFoundException("Not found with id: " + id);
    }
}
