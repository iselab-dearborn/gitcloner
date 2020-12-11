package edu.iselab.gitcloner.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import edu.iselab.gitcloner.config.AsyncConfiguration.TaskStatus;
import edu.iselab.gitcloner.persistence.entity.Project;

@Service
public class CloneService {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private TaskService taskService;
    
    @Async
    public void execute(Project project) {
        
        checkNotNull(project, "project should not be null");
        
        projectService.updateStatus(project, TaskStatus.RUNNING);
        
        Path directory = project.getDirectory();
        
        String taskId = project.getId().toString();

        try {
            
            if (Files.exists(directory)) {
                fetch(taskId, directory, project);
            } else {
                clone(taskId, directory, project);
            }
            
            projectService.updateStatus(project, TaskStatus.DONE);
            
        } catch (Exception ex) {
            
            taskService.error(taskId);
            
            project.setErrorMessage(ExceptionUtils.getStackTrace(ex));
            
            projectService.updateStatus(project, TaskStatus.ERROR);
            
            throw new RuntimeException(ex);
        }
    }
    
    private void fetch(String taskId, Path directory, Project project) throws Exception {
        
        checkArgument(!StringUtils.isEmpty(taskId), "taskId should not be blank");
        checkNotNull(directory, "directory should not be null");
        checkNotNull(project, "project should not be null");
        
        taskService.create(taskId, "Fetching " + project.getDisplayName());
        
        List<RefSpec> specs = new ArrayList<RefSpec>();
        
        specs.add(new RefSpec("+refs/heads/*:refs/remotes/origin/*"));
        specs.add(new RefSpec("+refs/tags/*:refs/tags/*"));
        specs.add(new RefSpec("+refs/notes/*:refs/notes/*"));
        
        Git git = Git.open(directory.toFile());
        
        FetchCommand fetch = git.fetch();
        
        fetch.setProgressMonitor(getTextProgressMonitor(taskId));
        fetch.setRefSpecs(specs);
        fetch.call();
        
        git.close();
        
        taskService.done(taskId);
    }
    
    private void clone(String taskId, Path directory, Project project) throws Exception {
        
        checkArgument(!StringUtils.isEmpty(taskId), "taskId should not be blank");
        checkNotNull(directory, "directory should not be null");
        checkNotNull(project, "project should not be null");
        
        taskService.create(taskId, "Cloning " + project.getDisplayName());
        
        Git git = Git.cloneRepository()
                .setURI(project.getGitUrl())
                .setProgressMonitor(getTextProgressMonitor(taskId))
                .setCloneAllBranches(true)
                .setDirectory(directory.toFile())
                .call();

        git.close();
        
        taskService.done(taskId);
    }
    
    private TextProgressMonitor getTextProgressMonitor(String taskId) {
        
        checkArgument(!StringUtils.isEmpty(taskId), "taskId should not be blank");
        
        return new TextProgressMonitor(new PrintWriter(System.out) {
            
            public void write(String description) {
                
                double progress = 90.0;

                if (description.contains("Enumerating objects")) {
                    progress = 20.0;
                } else if (description.contains("Receiving objects")) {
                    progress = 40.0;
                } else if (description.contains("Resolving deltas")) {
                    progress = 60.0;
                } else if (description.contains("Checking out files")) {
                    progress = 80.0;
                }
                
                taskService.update(taskId, description, progress);
            }
        });
    }
}
