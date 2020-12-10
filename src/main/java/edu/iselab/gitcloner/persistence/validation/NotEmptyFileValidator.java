package edu.iselab.gitcloner.persistence.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class NotEmptyFileValidator implements ConstraintValidator<NotEmptyFile, MultipartFile> {

    @Override
    public void initialize(NotEmptyFile contraint) {
        
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext cxt) {
        return !file.isEmpty();
    }
}
