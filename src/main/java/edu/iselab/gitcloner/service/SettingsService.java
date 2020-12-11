package edu.iselab.gitcloner.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@Service
public class SettingsService {

    @Value("${app.settings.output}")
    private Path output;

    @Value("${app.settings.core-pool-size}")
    private int corePoolSize = 2;

    @Value("${app.settings.max-pool-size}")
    private int maxPoolSize = 5;

    @PostConstruct
    public void setUp() {

        if (output == null) {
            output = Paths.get(System.getProperty("user.home"));
        }

        if (!Files.exists(output)) {
            throw new RuntimeException(String.format("The output folder '%s' does not exist", output));
        }

        if (corePoolSize <= 0) {
            throw new RuntimeException(String.format("The corePoolSize must be > 0 but it was %s", corePoolSize));
        }

        if (maxPoolSize <= 0) {
            throw new RuntimeException(String.format("The maxPoolSize must be > 0 but it was %s", maxPoolSize));
        }
        
        output = output.resolve("gitcloner");

        log.info("Settings");
        log.info("Output: {}", output);
        log.info("Core Pool Size: {}", corePoolSize);
        log.info("Max Pool Size: {}", maxPoolSize);
    }
}
