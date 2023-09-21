package com.paper.controllers;

import com.paper.domain.Image;
import com.paper.exceptions.ImageNotFoundException;
import com.paper.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

@RestController
@RequestMapping("/upload/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @CrossOrigin
    @GetMapping(value = "/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId) {
        if (ObjectId.isValid(imageId)) {
            Image image = imageRepository.findById(imageId).orElseThrow(ImageNotFoundException::new);
            String base64Image = image.getBase64Image();
            String payload = hasHeader(base64Image) ? getPayLoad(base64Image) : base64Image;

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getType()))
                    .body(Base64.getDecoder().decode(payload));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean hasHeader(String base64) {
        return base64.contains(",");
    }

    private String getPayLoad(String base64ImageWithHeader) {
        return base64ImageWithHeader.split(",")[1];
    }

}
