package edu.iselab.gitcloner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.iselab.gitcloner.service.ProjectService;

@Controller
public class IndexController {

    @Autowired
    private ProjectService projectService;
    
    @GetMapping("/")
    public String index(Model model) {
        
        model.addAttribute("projects", projectService.findAll());
        
        return "index";
    }
}
