package com.paper.services;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository machineRepository;

    @Override
    public ManufactureMachine save(ManufactureMachine machine, List<Image> images) {
        System.out.println(imageRepository.findAll());

        List<Image> savedImages = imageRepository.saveAll(images);
        System.out.println(imageRepository.findAll());
        List<String> ids = savedImages.stream().map(Image::getId).toList();
        machine.setImages(ids);
        return machineRepository.save(machine);
    }
}
