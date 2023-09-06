package com.paper.controllers;


import com.paper.repositories.GoodGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final GoodGroupRepository goodGroupRepository;

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("goodGroupList", goodGroupRepository.findAll());
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
