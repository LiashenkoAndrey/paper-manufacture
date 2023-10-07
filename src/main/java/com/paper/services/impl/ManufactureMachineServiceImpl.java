package com.paper.services.impl;

import com.github.dozermapper.core.loader.api.FieldsMappingOptions;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import com.paper.domain.Catalog;
import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.dto.MMDto;
import com.paper.dto.MMDto2;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Locale;

import static com.google.cloud.translate.Translate.TranslateOption.sourceLanguage;
import static com.google.cloud.translate.Translate.TranslateOption.targetLanguage;
import static com.paper.util.EntityMapper.map;

@Service
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ImageRepository imageRepository;

    private final ManufactureMachineRepository machineRepository;

    private final CatalogRepository catalogRepository;
    private final ProducerRepository producerRepository;

    private final Translate translate;

    private static final Logger logger = LogManager.getLogger(ManufactureMachine.class);
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


    String sourceLanguage = Locale.ENGLISH.getLanguage();

    @Override
    public List<MMDto2> translateAllNamesDto(List<MMDto2> machines) {
        String currentLocale = LocaleContextHolder.getLocale().getLanguage();
        logger.info("before: " + machines);
        List<String> names = machines.stream().map(MMDto2::getName).toList();

        if (!currentLocale.equals(sourceLanguage)) {
            List<Translation> namesTranslation = translate.translate(
                    names,
                    sourceLanguage("en"),
                    targetLanguage(currentLocale)
            );

            for (int i = 0; i < machines.size(); i++) {
                machines.get(i).setName(namesTranslation.get(i).getTranslatedText());
            }
        }
        logger.info("after: " +machines);
        return machines;
    }

    @Override
    public List<ManufactureMachine> translateAll(List<ManufactureMachine> machines) {
        String currentLocale = LocaleContextHolder.getLocale().getLanguage();

        if (!currentLocale.equals(sourceLanguage)) {
            List<String> names = machines.stream().map(ManufactureMachine::getName).toList();
            List<String> desc = machines.stream().map(ManufactureMachine::getDescription).toList();

            List<Translation> namesTranslated = translate.translate(names, sourceLanguage("en"), targetLanguage(currentLocale));
            List<Translation> descTranslated = translate.translate(desc, sourceLanguage("en"), targetLanguage(currentLocale));

            for (int i = 0; i < machines.size(); i++) {
                machines.get(i).setName(namesTranslated.get(i).getTranslatedText());
                machines.get(i).setDescription(descTranslated.get(i).getTranslatedText());
            }
        }
        return machines;
    }



    @Override
    public ManufactureMachine translate(ManufactureMachine machine) {
        String currentLocale = LocaleContextHolder.getLocale().getLanguage();
        if (!currentLocale.equals(sourceLanguage)) {
            var source = sourceLanguage("en");
            var target = targetLanguage(currentLocale);
            var name = translate.translate(machine.getName(), source, target);
            var desc = translate.translate(machine.getDescription(), source, target);

            machine.setName(name.getTranslatedText());
            machine.setDescription(desc.getTranslatedText());
        }
        return machine;
    }


    private boolean priceNotNull(Long from, Long to) {
        return from != null && to != null;
    }

}
