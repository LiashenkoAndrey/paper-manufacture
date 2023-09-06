package com.paper.repositories;

import com.paper.domain.Video;
import com.paper.domain.VideoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("select new com.paper.domain.VideoDto(v.id, v.name) from Video v")
    List<VideoDto> findAllDto();
}
