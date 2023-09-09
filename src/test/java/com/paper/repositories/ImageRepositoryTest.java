package com.paper.repositories;


import com.paper.domain.Image;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;


    @Test
    public void save() throws IOException {
        Image image = new Image(MediaType.IMAGE_JPEG_VALUE, Files.readAllBytes(Path.of("src/test/resources/testImage.jpg")));
        Image saved = imageRepository.save(image);
        System.out.println(saved.getId());
        assertTrue(imageRepository.existsById(saved.getId()));
    }


    @AfterAll
    public void after() {
        imageRepository.deleteAll();
    }
}
