package edu.iselab.gitcloner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.iselab.gitcloner.service.SettingsService;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;
    
    @GetMapping("")
    public String index(Model model) {
        
        model.addAttribute("settings", settingsService);
        
        return "settings";
    }
}
