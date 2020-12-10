package edu.iselab.gitcloner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.iselab.gitcloner.persistence.entity.Project;
import edu.iselab.gitcloner.service.ProjectService;
import edu.iselab.gitcloner.util.AlertUtils;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    @PostMapping("/delete/{id:.+}")
    public String deleteById(@PathVariable Long id, RedirectAttributes ra) {

        Project project = projectService.deleteById(id);
        
        AlertUtils.success(ra, "alert.project.removed.success", project.getDisplayName());

        return "redirect:/";
    }
    
    @GetMapping("/{id:.+}")
    public String get(@PathVariable Long id, Model model) {

        model.addAttribute("project", projectService.findById(id));

        return "project";
    }
}
