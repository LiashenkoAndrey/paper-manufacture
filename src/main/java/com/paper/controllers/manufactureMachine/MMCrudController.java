package com.paper.controllers.manufactureMachine;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.ManufactureMachineNotFoundException;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/good/manufacture-machine")
@RequiredArgsConstructor
public class MMCrudController {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository repository;

    private final ManufactureMachineService machineService;

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
}
