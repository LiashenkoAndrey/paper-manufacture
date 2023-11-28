package com.paper.controllers;


import com.paper.repositories.CatalogRepository;
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


    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("catalogs", catalogRepository.findAll());
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
