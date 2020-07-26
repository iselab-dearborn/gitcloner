package edu.iselab.cloner;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import edu.iselab.cloner.gui.MainWindow;
import edu.iselab.cloner.model.Project;
import edu.iselab.cloner.monitor.GitProgressMonitor;
import edu.iselab.cloner.monitor.ProgressListener;
import edu.iselab.cloner.task.CloneTask;
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
    
    /** Output folder where the data will be saved */
    @Option(names = { "-o", "--output" }, description = "Output folder where the data will be saved. Default: ${DEFAULT-VALUE}")
    protected Path output = Paths.get(System.getProperty("user.dir")).resolve("output");
    
    @Option(names = { "-g", "--gui" }, description = "Show the user interface. Default: ${DEFAULT-VALUE}")
    protected boolean isGui = false;
    
    @Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help message")
    protected boolean help = false;
    
    /** The number of threads. The value should be &#62; 1 */
    @Option(names = { "-t", "--threads" }, description = "The number of threads >= 1. Default: ${DEFAULT-VALUE}")
    protected int nThreads = 3;
    
    /** App's Version */
    public static final String version = Launcher.class.getPackage().getImplementationVersion();
    
    public static void main(String[] args) throws Exception {
        
        Launcher launcher = CommandLine.populateCommand(new Launcher(), args);
        
        if (launcher.help) {
            CommandLine.usage(launcher, System.out);
            return;
        }
        
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
        
        GitProgressMonitor progressListener = new GitProgressMonitor();
        
        progressListener.add(new ProgressListener() {
            
            @Override
            public void message(Project project, String message) {
                System.out.print(message);
            }
        });
        
        List<CloneTask> tasks = projects.stream()
                .map(e -> new CloneTask(e, output, progressListener))
                .collect(Collectors.toList());
                
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        if (isGui) {
            MainWindow.createAndShowGUI(projects, progressListener);
        }
        
        List<Future<Void>> futures = executor.invokeAll(tasks);
        
        for (Future<Void> future : futures) {
            future.get();
        }
        
        log.info("Done");
    }
}
