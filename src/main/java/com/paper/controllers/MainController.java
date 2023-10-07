package com.paper.controllers;


import com.paper.repositories.CatalogRepository;
import com.paper.services.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {


    private final CatalogRepository catalogRepository;


    private final CatalogService catalogService;


    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("catalogs", catalogService.translateAll(catalogRepository.findAll()));
        return "/main";
    }


    @GetMapping("/contacts")
    public String contacts() {
        return "/contacts";
    }

    @GetMapping("/video")
    public String video() {
        return "video";
    }
}
