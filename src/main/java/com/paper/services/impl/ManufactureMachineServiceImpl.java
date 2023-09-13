package com.paper.services.impl;

import com.paper.domain.Catalog;
import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ManufactureMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository machineRepository;

    private final CatalogRepository catalogRepository;
    private final ProducerRepository producerRepository;

    @Override
    public ManufactureMachine save(ManufactureMachine machine, ManufactureMachineDto dto) {
        List<Image> savedImages = imageRepository.saveAll(dto.getImages());
        List<String> ids = savedImages.stream().map(Image::getId).toList();
        machine.setImages(ids);
        Producer producer = producerRepository.findById(dto.getProducerId()).orElseThrow(ProducerNotFoundException::new);
        machine.setProducer(producer);

        Long catalogId = dto.getCatalogId();
        if (dto.getCatalogId() != null) {
            Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(CatalogNotFoundException::new);
            machine.setCatalog(catalog);
        }
        return machineRepository.save(machine);
    }
}
