package edu.iselab.gitcloner.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import edu.iselab.gitcloner.persistence.entity.Project;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    
    Optional<Project> findByGitUrl(String gitUrl);
}
