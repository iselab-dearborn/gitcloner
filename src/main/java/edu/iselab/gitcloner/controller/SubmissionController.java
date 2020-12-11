package edu.iselab.gitcloner.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.iselab.gitcloner.persistence.dto.SubmissionDTO;
import edu.iselab.gitcloner.persistence.entity.Project;
import edu.iselab.gitcloner.service.SettingsService;
import edu.iselab.gitcloner.service.SubmissionService;
import edu.iselab.gitcloner.util.AlertUtils;

@Controller
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;
    
    @Autowired
    private SettingsService settingsService;
    
    @GetMapping("/create")
    public String create(Model model, SubmissionDTO submissionDTO) {
        
        model.addAttribute("submissionDTO", submissionDTO);
        model.addAttribute("output", settingsService.getOutput());
        
        return "submit";
    }
 
    @GetMapping("/upload")
    public String upload() {
        return "redirect:/";
    }
    
    @PostMapping("/upload")
    public String upload(Model model, @Valid SubmissionDTO submissionDTO, BindingResult result, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return create(model, submissionDTO);
        }
        
        List<Project> added = submissionService.upload(submissionDTO);
        
        AlertUtils.success(ra, "alert.project.added.success", added.size());
        
        return "redirect:/";
    }
}
