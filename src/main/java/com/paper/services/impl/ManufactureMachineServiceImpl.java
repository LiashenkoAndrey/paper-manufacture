package com.paper.services.impl;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ManufactureMachineService;
import com.paper.services.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository machineRepository;

    private final ProducerRepository producerRepository;

    @Override
    public ManufactureMachine save(ManufactureMachine machine, List<Image> images, Long producerId) {
        List<Image> savedImages = imageRepository.saveAll(images);
        List<String> ids = savedImages.stream().map(Image::getId).toList();
        machine.setImages(ids);
        Producer producer = producerRepository.findById(producerId).orElseThrow(ProducerNotFoundException::new);
        machine.setProducer(producer);
        return machineRepository.save(machine);
    }
}
