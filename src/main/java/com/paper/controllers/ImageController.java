package com.paper.controllers;

import com.paper.domain.MongoImage;
import com.paper.exceptions.ImageNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@Log4j2
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/upload/image/{imageId}")
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


    @PostMapping("/protected/upload/image/new")
    @PreAuthorize("hasAuthority('manage:images')")
    public MongoImage addNew(@RequestParam("file") MultipartFile file) {
        return imageRepository.save(new MongoImage(Objects.requireNonNull(file.getContentType()), ServiceUtil.toBinary(file)));
    }

    @DeleteMapping("/protected/upload/image/delete/{id}")
    @PreAuthorize("hasAuthority('manage:images')")
    public void addNew(@PathVariable("id") String id) {
        imageRepository.deleteById(id);
    }
}
