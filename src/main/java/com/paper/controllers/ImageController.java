package com.paper.controllers;

import com.paper.domain.MongoImage;
import com.paper.exceptions.ImageNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/upload/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    private static final Logger logger = LogManager.getLogger(ImageRepository.class);

    @GetMapping(value = "/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId) {
        if (ObjectId.isValid(imageId)) {
            MongoImage mongoImage = imageRepository.findById(imageId).orElseThrow(ImageNotFoundException::new);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mongoImage.getType()))
                    .body(mongoImage.getImage().getData());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("new")
    private String addNew(@RequestParam("file") MultipartFile file) {
        MongoImage image = imageRepository.save(new MongoImage(Objects.requireNonNull(file.getContentType()), ServiceUtil.toBinary(file)));
        return image.getId();
    }

    @DeleteMapping("/delete/{id}")
    private void addNew(@PathVariable("id") String id) {
        imageRepository.deleteById(id);
    }
}
