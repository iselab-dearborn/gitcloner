package edu.iselab.cloner;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import edu.iselab.cloner.model.Project;
import edu.iselab.cloner.util.GitUtils;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@Slf4j
@ToString
public class Launcher {
    
    /** File to be processed */
    @Option(names = { "-i", "--input" }, description = "File to be processed. Default: ${DEFAULT-VALUE}")
    protected Path input = Paths.get(System.getProperty("user.dir")).resolve("repos.txt");
    
    /** Output folder where the data will be storaged */
    @Option(names = { "-o", "--output" }, description = "Output folder where the data will be storaged. Default: ${DEFAULT-VALUE}")
    protected Path output = Paths.get(System.getProperty("user.dir")).resolve("output");
    
    /** App's Version */
    public static final String version = Launcher.class.getPackage().getImplementationVersion();
    
    public static void main(String[] args) throws Exception {
        
        Launcher launcher = CommandLine.populateCommand(new Launcher(), args);
        
        launcher.run();
    }
    
    public void printHeader() {
        
        log.info("***************************************");
        log.info("Welcome to Cloner v{}", version);
        log.info("***************************************");

        log.info("Parameters");
        log.info("Input: {}", input);
        log.info("Output: {}", output);
        
        log.info("***************************************");
    }
    
    public void run() throws Exception {
        
        printHeader();

        if (!Files.exists(input)) {
            throw new IllegalArgumentException(String.format("The file %s does not exists", input));
        }

        List<String> lines = Files.readAllLines(input, Charset.forName("UTF-8"));

        lines = lines.stream()
                .filter(e -> e != null)
                .map(e -> e.trim())
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());
        
        if (lines.isEmpty()) {
            throw new IllegalArgumentException(String.format("The file %s is empty", input));
        }
        
        List<Project> projects = lines.stream()
                .map(e -> GitUtils.parseOwnerAndRepo(e))
                .collect(Collectors.toList());
        
        log.info("Projects found: {}", projects.size());
        
        if (!Files.exists(output)) {  
            Files.createDirectories(output);
        }
        
        for (int i = 0; i < projects.size(); i++) {

            Project project = projects.get(i);

            log.info("[{}/{}] Cloning \"{}\"", i + 1, projects.size(), project.getDirectory());

            GitUtils.clone(project, output);
        }
        
        log.info("Done");
    }
}
