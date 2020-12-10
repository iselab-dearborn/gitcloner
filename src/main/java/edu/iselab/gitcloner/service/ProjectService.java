package edu.iselab.gitcloner.service;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.iselab.gitcloner.config.AsyncConfiguration.TaskStatus;
import edu.iselab.gitcloner.persistence.entity.Project;
import edu.iselab.gitcloner.persistence.repository.ProjectRepository;
import edu.iselab.gitcloner.util.FileUtils;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    public Iterable<Project> findAll(){
        return projectRepository.findAll();
    }
    
    public Project save(Project project) {
        
        checkNotNull(project, "project should not be null");
        
        return projectRepository.save(project);
    }
    
    public boolean contains(Project project) {

        checkNotNull(project, "project should not be null");

        return projectRepository.findByGitUrl(project.getGitUrl()).isPresent();
    }

    public void updateStatus(Project project, TaskStatus cloneStatus) {
        
        checkNotNull(project, "project should not be null");
        
        project.setCloneStatus(cloneStatus);
        
        save(project);
    }
    
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Project deleteById(Long id) {
        
        Project project = findById(id);

        // Firstly we need to remove the data saved on the database
        projectRepository.deleteById(id);
        
        // Secondly we need to remove to files related to it
        FileUtils.delete(project.getDirectory());
        
        return project;
    }
}
