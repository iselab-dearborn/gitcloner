package edu.iselab.cloner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Project {

    protected String gitUrl;

    protected String owner;

    protected String repo;

    public String getDirectory() {
        return String.format("%s.%s", owner, repo);
    }
}
