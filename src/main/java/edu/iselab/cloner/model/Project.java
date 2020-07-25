package edu.iselab.cloner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class that represents a project. It saves information such as git url,
 * owner, and repo name.
 *
 * @author Thiago Ferreira
 * @since 2020-08-25
 * @version 1.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Project {

    /** Git URL */
    protected String gitUrl;

    /** Owner of the repo */
    protected String owner;

    /** Repo name */
    protected String repo;

    /**
     * Get the name of the directory when the project is cloned. Currently, it is
     * based on the owner and repo and follows the pattern "{owner}.{repo}"
     * 
     * @return the directory name
     */
    public String getDirectory() {
        return String.format("%s.%s", owner, repo);
    }
}
