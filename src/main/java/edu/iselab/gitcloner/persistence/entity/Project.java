package edu.iselab.gitcloner.persistence.entity;

import java.nio.file.Path;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import com.sun.istack.NotNull;

import edu.iselab.gitcloner.config.AsyncConfiguration.TaskStatus;
import edu.iselab.gitcloner.config.JPAConfiguration.Auditable;
import edu.iselab.gitcloner.persistence.converter.PathConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Project extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @URL
    @Column(unique = true)
    private String gitUrl;

    @NotBlank
    private String owner;

    @NotBlank
    private String repoName;

    @Convert(converter = PathConverter.class)
    @NotNull
    private Path directory;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus cloneStatus = TaskStatus.PENDING;

    @Lob
    private String errorMessage;
    
    public String getDisplayName() {
        return String.format("%s/%s", owner, repoName);
    }
    
    public String getDirectoryName() {
        return String.format("%s@%s", owner, repoName);
    }
}
