package edu.iselab.cloner.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;

import edu.iselab.cloner.model.Project;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitUtils {
    
    public static final Pattern gitUrlRegex = Pattern.compile("^(https|git)(:\\/\\/|@)([^\\/:]+)[\\/:]([^\\/:]+)\\/(.+).git$");

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
    
    public static void clone(Path output, Project project) {
        
        Path directory = output.resolve(project.getDirectory());

        if (Files.exists(directory)) {
            return;
        }
        
        try {
            Git.cloneRepository()
                .setURI(project.getGitUrl())
                .setDirectory(directory.toFile()).call();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } 
    }
}
