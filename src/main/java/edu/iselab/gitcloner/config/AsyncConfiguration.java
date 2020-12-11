package edu.iselab.gitcloner.config;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import edu.iselab.gitcloner.service.SettingsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Task {

        private String id = UUID.randomUUID().toString();

        private String title;

        private String description;

        private double progress;
    }
    
    @Getter
    public enum TaskStatus {
        
        PENDING(""),
        STARTING(""),
        RUNNING(""),
        DONE("text-success"),
        ERROR("text-danger");
        
        private final String color;
        
        TaskStatus(String color) {
            this.color = color;
        }
    }
    
    
    @Autowired
    private SettingsService settingsService;
    
    private Map<String, Task> runningTasks = new ConcurrentHashMap<>();
    
    @Override
    public Executor getAsyncExecutor() {
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // corePoolSize is the minimum number of workers to keep alive
        executor.setCorePoolSize(settingsService.getCorePoolSize());
        // maxPoolSize defines the maximum number of threads that can ever be created. 
        executor.setMaxPoolSize(settingsService.getMaxPoolSize());
        executor.setThreadNamePrefix("Task-");
        executor.initialize();
        
        return executor;
    }
    
    @Bean
    public Map<String, Task> runningTasks() {
        return runningTasks;
    }
}
