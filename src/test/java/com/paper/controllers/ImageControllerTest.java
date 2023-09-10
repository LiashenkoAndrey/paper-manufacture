package com.paper.controllers;

import com.paper.domain.Image;
import com.paper.repositories.ImageRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeAll
    public void before() throws IOException {
        Image image1 = Image.builder()
                .base64Image(Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"))))
                .type(MediaType.IMAGE_JPEG_VALUE)
                .id("64fb5c6d490a243ee76dc7cb")
                .build();

        Image image2 = Image.builder()
                .base64Image(Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/test/resources/testImage.png"))))
                .type(MediaType.IMAGE_PNG_VALUE)
                .id("64fb5c6d490a243ee76dc7ac")
                .build();

        imageRepository.saveAll(List.of(image1, image2));
    }

    @Test
    @Order(1)
    public void getImage() throws Exception {

        mockMvc.perform(get("/upload/image/" + "64fb5c6d490a243ee76dc7cb"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG_VALUE));

        mockMvc.perform(get("/upload/image/" + "64fb5c6d490a243ee76dc7ac"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_PNG_VALUE));

        mockMvc.perform(get("/upload/image/" + "64fc5c6d390a243ee76dc7ac"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/upload/image/" + "zxczxczczczcxzxczxczxczc"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/upload/image/" + ")c_"))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    public void deleteAll() {
        imageRepository.deleteAll();
    }

}
