package com.paper.controllers.manufactureMachine;


import com.paper.domain.CatalogType;
import com.paper.dto.CatalogWithGoodsCountDto;
import com.paper.repositories.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MMCatalog {

    private final CatalogRepository catalogRepository;


    @GetMapping("/good/manufacture-machine/catalog/all")
    public @ResponseBody List<CatalogWithGoodsCountDto> getAllCatalogs() {
        return catalogRepository.findAllByType(CatalogType.MANUFACTURE_MACHINE.toString());
    }
}
