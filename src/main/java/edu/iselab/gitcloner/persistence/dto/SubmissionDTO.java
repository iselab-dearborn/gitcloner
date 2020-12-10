package edu.iselab.gitcloner.persistence.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import edu.iselab.gitcloner.persistence.validation.ContentType;
import edu.iselab.gitcloner.persistence.validation.NotEmptyFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SubmissionDTO {
    
    @NotEmptyFile
    @ContentType("text/plain")
    @NotNull
    private MultipartFile file;
}
