package com.paper.controllers.manufactureMachine;

import com.paper.domain.ManufactureMachine;
import com.paper.domain.MongoImage;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/manufacture-machine/image")
@RequiredArgsConstructor
public class MMImageController {

    private static final Logger logger = LogManager.getLogger(MMImageController.class);

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository machineRepository;

    @PostMapping("/new")
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
