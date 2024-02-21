package com.paper.controllers.good;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Log4j2
@RequestMapping("/api")
@RequiredArgsConstructor
public class MMCrudController {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository repository;

    private final ManufactureMachineService machineService;

    @PostMapping(value = "/protected/manufacture-machine/new")
    @PreAuthorize("hasAuthority('manage:goods')")
    public ManufactureMachine create(@ModelAttribute ManufactureMachineDto dto) {
        if (dto.getImages().isEmpty() && dto.getExternalImagesUrls().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It needs at least a one image to save a good");
        }

        ManufactureMachine machine = machineService.save(dto);
        log.info("saved = {}", machine);
        return machine;
    }


    @DeleteMapping("/protected/manufacture-machine/{id}/delete")
    @PreAuthorize("hasAuthority('manage:goods')")
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

    @PostMapping("/protected/manufacture-machine/{goodId}/property/new")
    @PreAuthorize("hasAuthority('manage:goods')")
    public void newProperty(@PathVariable("goodId") Long goodId,
                            @RequestParam("name") String name,
                            @RequestParam("value") String value) {

        machineService.addProperty(goodId, name, value);
    }

    @DeleteMapping("/protected/manufacture-machine/{goodId}/property/delete")
    @PreAuthorize("hasAuthority('manage:goods')")
    public void deleteProperty(@PathVariable("goodId") Long goodId,
                            @RequestParam("name") String name) {

        machineService.deleteProperty(goodId, name);
    }
}
