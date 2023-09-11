package com.paper.services.impl;

import com.paper.domain.Image;
import com.paper.domain.Producer;
import com.paper.dto.ProducerDto;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final ImageRepository imageRepository;

    private final ProducerRepository producerRepository;

    @Override
    public Producer save(ProducerDto producerDto) {
        Image saved = imageRepository.save(producerDto.getImage());
        Producer producer = producerDto.getProducer();
        producer.setLogotypeId(saved.getId());
        return producerRepository.save(producerDto.getProducer());
    }

    @Override
    public void deleteById(Long id) {
        Producer producer = producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);
        imageRepository.deleteById(producer.getLogotypeId());
        producerRepository.deleteById(id);
    }
}
