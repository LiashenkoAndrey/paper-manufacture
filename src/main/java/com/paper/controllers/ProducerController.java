package com.paper.controllers;

import com.paper.domain.Producer;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    private final ProducerRepository producerRepository;

    @PostMapping("/protected/producer/new")
    @PreAuthorize("hasAuthority('manage:producers')")
    public void create(@ModelAttribute Producer producer) {
        log.info("New producer: " + producer );
        producerService.save(producer);
    }

    @GetMapping(value = "/public/producer/all")
    public ResponseEntity<List<Producer>> getAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(producerRepository.findAll());
    }

    @GetMapping("/producer/{id}")
    public Producer getInfo(@PathVariable("id") Long id) {
        return producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);
    }

    @PutMapping("/protected/producer/update")
    @PreAuthorize("hasAuthority('manage:producers')")
    private void update(@RequestParam("name") String name,
                        @RequestParam("id") Long id,
                        @RequestParam("description") String desc,
                        @RequestParam("websiteUrl") String website) {
        log.info("Edit a producer: ", name, id, desc, website);
        Producer producer = producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);

        producer.setDescription(desc);
        producer.setName(name);
        producer.setWebsiteUrl(website);

        producerRepository.save(producer);
    }

    @DeleteMapping("/protected/producer/delete")
    @PreAuthorize("hasAuthority('manage:producers')")
    private void delete(@RequestParam("id") Long id) {
        if (producerRepository.existsById(id)) {
            producerRepository.deleteById(id);
        } else throw new ProducerNotFoundException("Not found with id: " + id);
    }
}
