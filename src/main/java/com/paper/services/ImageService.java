package com.paper.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    List<String> saveAll(List<MultipartFile> files);
}
