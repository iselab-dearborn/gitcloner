package edu.iselab.gitcloner.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edu.iselab.gitcloner.config.AsyncConfiguration.Task;
import edu.iselab.gitcloner.config.AsyncConfiguration.TaskStatus;

@Service
public class TaskService {
    
    @Autowired
    private Map<String, Task> runningTasks;
    
    @Autowired
    private SimpMessagingTemplate template;
    
    public String create(String taskId, String title) {

        Task task = new Task();

        task.setId(taskId);
        task.setTitle(title);
        task.setDescription(TaskStatus.STARTING.name());
        task.setProgress(0.0);

        runningTasks.put(task.getId(), task);
        
        template.convertAndSend("/topic/running-tasks/create", task);

        return task.getId();
    }
    
    public void update(String taskId, String description, double progress) {
        
        Task task = runningTasks.get(taskId);

        task.setProgress((int) progress);  
        task.setDescription(description);
            
        template.convertAndSend("/topic/running-tasks/update", task);
    }
    
    public void error(String taskId) {
        
        Task task = runningTasks.get(taskId);

        task.setProgress(-1);      
        task.setDescription(TaskStatus.ERROR.name());
        
        template.convertAndSend("/topic/running-tasks/error", task);
    }
    
    public void done(String taskId) {

        Task task = runningTasks.get(taskId);

        task.setProgress(100.0);
        task.setDescription(TaskStatus.DONE.name());
        
        template.convertAndSend("/topic/running-tasks/done", task);
    }
    
    public void updateAll() {

        for (Task task : runningTasks.values()) {
            
            template.convertAndSend("/topic/running-tasks/update", task);
        }
    }
}
