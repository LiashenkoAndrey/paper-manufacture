package com.paper.services.impl;

import com.paper.domain.Producer;
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
    public Producer save(Producer producer) {
        return producerRepository.save(producer);
    }

    @Override
    public void deleteById(Long id) {
        Producer producer = producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);
        imageRepository.deleteById(producer.getLogotypeId());
        producerRepository.deleteById(id);
    }
}
