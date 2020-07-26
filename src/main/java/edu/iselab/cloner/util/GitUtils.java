package edu.iselab.cloner.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ProgressMonitor;

import edu.iselab.cloner.model.Project;
import edu.iselab.cloner.task.CloneTask;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for deal with git commands
 *
 * @author Thiago Ferreira
 * @since 2020-08-25
 * @version 1.0
 */
@Slf4j
public class GitUtils {
    
    /**
     * The regex pattern used for validating a git url. It should follow ^(https|git)(:\\/\\/|@)([^\\/:]+)[\\/:]([^\\/:]+)\\/(.+).git$
     */
    public static final Pattern gitUrlRegex = Pattern.compile("^(https|git)(:\\/\\/|@)([^\\/:]+)[\\/:]([^\\/:]+)\\/(.+).git$");

    /**
     * Private Constructor. Since this class is a utility one, it is allowed to
     * create an instance of it
     */
    private GitUtils() {
        throw new AssertionError("Not allowed");
    }
    
    /**
     * Parse a git URL and return an instance of {@link Project}. If the url is invalid, an
     * RuntimeException is threw.
     * 
     * @param gitUrl the gir url to be processed. It should not be null or empty, and
     *               follow the pattern {@link GitUtils#gitUrlRegex}
     * @return an instance of {@link Project}.
     */
    public static Project parseOwnerAndRepo(String gitUrl) {
        
        checkNotNull(gitUrl, "gitUrl should not be null");
        checkArgument(!gitUrl.trim().isEmpty(), "gitUrl should not be empty");
        
        log.debug("Parsing owner and repo for {}", gitUrl);

        Matcher m = gitUrlRegex.matcher(gitUrl);

        if (m.find()) {
            
            return new Project(
                gitUrl,
                m.group(4),
                m.group(5)
            );
        }

        throw new RuntimeException(String.format("%s is not in a valid git url", gitUrl));
    }
    
    /**
     * Clone a {@link Project} given an output folder
     *
     * @param project the {@link Project} to be cloned. It should not be null
     * @param output the output folder. It should not be null
     * @param progressMonitor the progress monitor. It should not be null
     */
    public static void clone(Project project, Path output, ProgressMonitor progressMonitor) {
        
        checkNotNull(output, "output should not be null");
        checkNotNull(project, "project should not be null");
        checkNotNull(progressMonitor, "progressMonitor should not be null");
        
        Path directory = output.resolve(project.getDirectory());

        if (Files.exists(directory)) {
            
            if (progressMonitor instanceof CloneTask) {
                ((CloneTask) progressMonitor).send("It is already cloned");
            }
            
            return;
        }
        
        try {
            Git.cloneRepository()
                .setURI(project.getGitUrl())
                .setProgressMonitor(progressMonitor)
                .setDirectory(directory.toFile()).call();
        } catch (Exception ex) {
            
            if (progressMonitor instanceof CloneTask) {
                ((CloneTask) progressMonitor).send("Exception: " + ex.getMessage());
            }
            
            log.error(ex.getMessage(), ex);
        } 
    }
}
