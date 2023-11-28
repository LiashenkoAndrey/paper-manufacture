package com.paper.services.impl;

import com.paper.domain.MongoImage;
import com.paper.repositories.ImageRepository;
import com.paper.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.paper.util.ServiceUtil.toBinary;

@Service
@RequiredArgsConstructor
public class MongoImageService implements ImageService {

    private static final Logger log = LogManager.getLogger(MongoImageService.class);
    private final ImageRepository imageRepository;

    @Override
    public List<String> saveAll(List<MultipartFile> files) {
        List<MongoImage> mongoImages = files.stream().map((file) -> new MongoImage("png", toBinary(file))).toList();
        List<MongoImage> savedMongoImages = imageRepository.saveAll(mongoImages);
        return savedMongoImages.stream().map(MongoImage::getId).toList();
    }

}
