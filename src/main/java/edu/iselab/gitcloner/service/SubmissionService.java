package edu.iselab.gitcloner.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.iselab.gitcloner.persistence.dto.SubmissionDTO;
import edu.iselab.gitcloner.persistence.entity.Project;
import edu.iselab.gitcloner.util.FileUtils;
import edu.iselab.gitcloner.util.GitUtils;

@Service
public class SubmissionService {
    
    @Autowired
    private CloneService cloneService;
    
    @Autowired
    private ProjectService projectService;
    
    @Value("${app.output}")
    private String output;
    
    public List<Project> upload(SubmissionDTO submissionDTO) {
        
        checkNotNull(submissionDTO, "submissionDTO should not be null");
        
        if (StringUtils.isBlank(output)) {
            output = System.getProperty("user.home");
        }
        
        System.out.println(output);
        
        Path targetDirectory = Paths.get(output).resolve("gitcloner");

        FileUtils.createFolderIfNotExists(targetDirectory);
        
        MultipartFile file = submissionDTO.getFile();

        String content = FileUtils.readContentAsString(file);

        String[] lines = content.split("\n");

        List<Project> projects = new ArrayList<>();

        for (String line : lines) {

            if (!StringUtils.isBlank(line)) {

                Project project = GitUtils.parseOwnerAndRepo(line);

                project.setDirectory(targetDirectory.resolve(project.getDirectoryName()));

                projects.add(project);
            }
        }
        
        List<Project> addedProjects = new ArrayList<>();

        for (Project project : projects) {

            if (projectService.contains(project)) {
                continue;
            }

            project = projectService.save(project);

            cloneService.execute(targetDirectory, project);

            addedProjects.add(project);
        }

        return addedProjects;
    }
}
