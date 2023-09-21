package com.paper.services.impl;

import com.github.dozermapper.core.loader.api.FieldsMappingOptions;
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
import com.paper.util.MapConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.paper.util.EntityMapper.map;

@Service
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository machineRepository;

    private final CatalogRepository catalogRepository;
    private final ProducerRepository producerRepository;

    @Override
    public ManufactureMachine save(ManufactureMachineDto dto) {
        ManufactureMachine machine = dto.getManufactureMachine();
        List<Image> savedImages = imageRepository.saveAll(dto.getImages());
        List<String> idList = savedImages.stream().map(Image::getId).toList();
        machine.setImages(idList);

        Producer producer = producerRepository.findById(dto.getProducerId()).orElseThrow(ProducerNotFoundException::new);
        machine.setProducer(producer);

        Long catalogId = dto.getCatalogId();
        if (dto.getCatalogId() != null) {
            Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(CatalogNotFoundException::new);
            machine.setCatalog(catalog);
        }
        return machineRepository.save(machine);
    }

    @Override
    public void update(ManufactureMachine saved, ManufactureMachineDto dto) {
        map(dto.getManufactureMachine(), saved)
                .setMappingForFields("properties", "properties", FieldsMappingOptions.customConverter(MapConverter.class))
                .mapEmptyString(false)
                .mapNull(false)
                .map();

        if (dto.getCatalogId() != null) {
            Catalog catalog = catalogRepository.findById(dto.getCatalogId())
                    .orElseThrow(CatalogNotFoundException::new);
            saved.setCatalog(catalog);
        }

        if (dto.getProducerId() != null) {
            Producer producer = producerRepository.findById(dto.getProducerId())
                    .orElseThrow(ProducerNotFoundException::new);
            saved.setProducer(producer);
        }

        machineRepository.save(saved);
    }

    @Override
    public void deleteImageById(String imageId) {
        imageRepository.deleteById(imageId);
        machineRepository.deleteGoodImageById(imageId);
    }

}
