package com.paper.controllers.manufactureMachine;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/manufacture-machine")
@RequiredArgsConstructor
public class MMCrudController {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository repository;

    private final ManufactureMachineService machineService;

    @PostMapping(value = "/new")
    public Long create(@ModelAttribute ManufactureMachineDto dto) {

        if (dto.getImages().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It needs at least a one image to save a good");
        }

        ManufactureMachine saved = machineService.save(dto);
        return saved.getId();
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

    @PostMapping("/{goodId}/property/new")
    public void newProperty(@PathVariable("goodId") Long goodId,
                            @RequestParam("name") String name,
                            @RequestParam("value") String value) {

        machineService.addProperty(goodId, name, value);
    }

    @DeleteMapping("/{goodId}/property/delete")
    public void deleteProperty(@PathVariable("goodId") Long goodId,
                            @RequestParam("name") String name) {

        machineService.deleteProperty(goodId, name);
    }
}
