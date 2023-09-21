package com.paper.controllers.manufactureMachine;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/good/manufacture-machine/image")
@RequiredArgsConstructor
public class MMImageController {

    private static final Logger logger = LogManager.getLogger(MMImageController.class);

    private final ImageRepository imageRepository;

    private final ManufactureMachineService machineService;

    private final ManufactureMachineRepository machineRepository;

    @PostMapping("/new")
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile file,
                                   @RequestParam("goodId") Long goodId) throws IOException {

        String type = MediaType.parseMediaType(file.getContentType()).toString();
        Image image = Image.builder()
                .base64Image(Base64.getEncoder().encodeToString(file.getBytes()))
                .type(type)
                .build();

        Image savedImage = imageRepository.save(image);
        ManufactureMachine good = machineRepository.findById(goodId)
                .orElseThrow(ManufactureMachineNotFoundException::new);

        good.getImages().add(savedImage.getId());
        machineRepository.save(good);
        return ResponseEntity.ok(savedImage.getId());
    }

    @DeleteMapping("/{id}/delete")
    private ResponseEntity<?> deleteImage(@PathVariable("id") String imageId) {
        if (imageRepository.existsById(imageId)) {
            machineService.deleteImageById(imageId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
