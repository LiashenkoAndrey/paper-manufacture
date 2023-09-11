package com.paper.controllers;

import com.paper.domain.Video;
import com.paper.dto.VideoDto;
import com.paper.repositories.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoRepository videoRepository;

    @PostMapping("/new")
    public void save(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description) throws IOException {


        videoRepository.save(Video.builder()
                        .description(description)
                        .name(name)
                        .data(file.getBytes())
                .build());
    }

    @GetMapping("/upload")
    public ResponseEntity<Resource> get(@RequestParam("id") Long videoId) throws SQLException {
        Video video = videoRepository.findById(videoId).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(new ByteArrayResource(video.getData()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<VideoDto>> all() {
        return ResponseEntity.ok(videoRepository.findAllDto());
    }

    @DeleteMapping("/delete")
    public void delete() {

    }

    @PutMapping("/update")
    public void update() {

    }
}
