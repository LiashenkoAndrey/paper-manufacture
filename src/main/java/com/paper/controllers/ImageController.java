package com.paper.controllers;

import com.paper.domain.Image;
import com.paper.exceptions.ImageNotFoundException;
import com.paper.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/upload/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @CrossOrigin
    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId) {
        if (ObjectId.isValid(imageId)) {

            Image image = imageRepository.findById(imageId).orElseThrow(ImageNotFoundException::new);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getType()))
                    .body(image.getImage());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
