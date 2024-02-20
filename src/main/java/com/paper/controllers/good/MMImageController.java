package com.paper.controllers.good;

import com.paper.domain.ManufactureMachine;
import com.paper.domain.MongoImage;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@RequestMapping("/api/protected/manufacture-machine/image")
@RequiredArgsConstructor
public class MMImageController {


    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository machineRepository;

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('manage:goods')")
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile file,
                                   @RequestParam("goodId") Long goodId) {

        String type = MediaType.parseMediaType(file.getContentType()).toString();
        MongoImage mongoImage = MongoImage.builder()
                .image(ServiceUtil.toBinary(file))
                .type(type)
                .build();

        MongoImage savedMongoImage = imageRepository.save(mongoImage);
        ManufactureMachine good = machineRepository.findById(goodId)
                .orElseThrow(ManufactureMachineNotFoundException::new);

        good.getImages().add(savedMongoImage.getId());
        machineRepository.save(good);
        return ResponseEntity.ok(savedMongoImage.getId());
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('manage:goods')")
    private ResponseEntity<?> deleteImage(@PathVariable("id") String imageId, @RequestParam("mmId") Long machineId) {
        if (imageRepository.existsById(imageId)) {
            imageRepository.deleteById(imageId);

            ManufactureMachine machine = machineRepository.findById(machineId).orElseThrow(ManufactureMachineNotFoundException::new);
            machine.getImages().remove(imageId);
            machineRepository.save(machine);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
