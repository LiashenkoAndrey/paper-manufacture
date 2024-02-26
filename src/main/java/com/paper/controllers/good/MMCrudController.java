package com.paper.controllers.good;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.GoodDto;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api")
@RequiredArgsConstructor
public class MMCrudController {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository repository;

    private final ManufactureMachineService machineService;
    private final CatalogRepository catalogRepository;

    @PostMapping(value = "/protected/good/new")
    @PreAuthorize("hasAuthority('manage:goods')")
    public ManufactureMachine create(@Valid @ModelAttribute GoodDto dto) {
        if (dto.getImages().isEmpty() && dto.getExternalImagesUrls().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It needs at least a one image to save a good");
        }

        ManufactureMachine machine = machineService.save(dto);
        log.info("saved = {}", machine);
        return machine;
    }


    @DeleteMapping("/protected/good/{id}/delete")
    @PreAuthorize("hasAuthority('manage:goods')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        var manufactureMachine = repository
                .findById(id).orElseThrow(ManufactureMachineNotFoundException::new);
        if (!repository.existsById(id)) {
            return ResponseEntity.badRequest().body("Not exist");
        } else {
            repository.deleteById(id);
            for (String image : manufactureMachine.getImages()) {
                imageRepository.deleteById(image);
            }
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("protected/good/{id}/update")
    @PreAuthorize("hasAuthority('manage:goods')")
    public ManufactureMachine updateGood(@Valid @ModelAttribute GoodDto dto) {
        log.info("dto = {}", dto);
        if (repository.existsById(dto.getId())) {
            ManufactureMachine machine = repository.findById(dto.getId()).orElseThrow(ManufactureMachineNotFoundException::new);
            machine.setExternalImages(dto.getExternalImagesUrls());
            machine.setName(dto.getName());
            machine.setPrice(dto.getPrice());
            machine.setUrl(dto.getUrl());
            machine.setOldPrice(dto.getOldPrice());
            machine.setVideoUrl(dto.getVideoUrl());
            machine.setCatalog(catalogRepository.getReferenceById(dto.getCatalogId()));
            log.info("result = {}", machine);
            return repository.save(machine);
        }
        throw new ManufactureMachineNotFoundException("not exist");

    }

    @PostMapping("/protected/good/{goodId}/property/new")
    @PreAuthorize("hasAuthority('manage:goods')")
    public void newProperty(@PathVariable("goodId") Long goodId,
                            @RequestParam("name") String name,
                            @RequestParam("value") String value) {

        machineService.addProperty(goodId, name, value);
    }

    @DeleteMapping("/protected/good/{goodId}/property/delete")
    @PreAuthorize("hasAuthority('manage:goods')")
    public void deleteProperty(@PathVariable("goodId") Long goodId,
                            @RequestParam("name") String name) {

        machineService.deleteProperty(goodId, name);
    }
}
