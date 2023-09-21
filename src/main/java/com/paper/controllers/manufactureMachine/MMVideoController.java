package com.paper.controllers.manufactureMachine;

import com.paper.domain.Video;
import com.paper.exceptions.ManufactureMachineNotFoundException;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;

@Controller
@RequestMapping("/good/manufacture-machine/video")
@RequiredArgsConstructor
public class MMVideoController {

    private final ManufactureMachineRepository repository;

    private final VideoRepository videoRepository;
    private static final Logger logger = LogManager.getLogger(MMVideoController.class);

    @PostMapping("/new")
    public ResponseEntity<?> addVideo(@RequestParam("video") MultipartFile file,
                                      @RequestParam("duration") LocalTime duration,
                                      @RequestParam("goodId") Long goodId) throws IOException {
        Video video = Video.builder()
                .data(file.getBytes())
                .name("test")
                .description("desctiption")
                .duration(duration)
                .build();

        logger.info(video);
        var good = repository.findById(goodId).orElseThrow(ManufactureMachineNotFoundException::new);
        good.getVideos().add(video);
        repository.save(good);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    private ResponseEntity<?> deleteVideo(@PathVariable("id") Long videoId) {
        if (videoRepository.existsById(videoId)) {
            videoRepository.deleteById(videoId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
