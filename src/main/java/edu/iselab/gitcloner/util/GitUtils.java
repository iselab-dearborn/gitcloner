package edu.iselab.gitcloner.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.iselab.gitcloner.persistence.entity.Project;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitUtils {

    public static final String gitUrlRegex = "^(https|git)(:\\/\\/|@)([^\\/:]+)[\\/:]([^\\/:]+)\\/(.+).git$";
    
    public static final Pattern gitUrlPattern = Pattern.compile(gitUrlRegex);

    public static Project parseOwnerAndRepo(String gitUrl) {
        
        checkNotNull(gitUrl, "gitUrl should not be null");
        checkArgument(!StringUtils.isEmpty(gitUrl), "gitUrl should not be blank");
        
        log.debug("Parsing owner and repo for {}", gitUrl);

        Matcher m = gitUrlPattern.matcher(gitUrl);

        if (m.find()) {
            
            Project project = new Project();

            project.setGitUrl(gitUrl);
            project.setOwner(m.group(4));
            project.setRepoName(m.group(5));
                
            return project;
        }

        throw new RuntimeException(String.format("'%s' is not in a valid format", gitUrl));
    }
}
