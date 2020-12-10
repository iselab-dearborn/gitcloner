package edu.iselab.gitcloner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import edu.iselab.gitcloner.service.TaskService;

@Controller
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @MessageMapping("/running-tasks/join")
    public void join(SimpMessageHeaderAccessor headerAccessor) {
        taskService.updateAll();
    }
}
