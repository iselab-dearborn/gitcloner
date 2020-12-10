package edu.iselab.gitcloner.persistence.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class ContentTypeValidator implements ConstraintValidator<ContentType, MultipartFile> {

    private ContentType contraint;

    @Override
    public void initialize(ContentType contraint) {
        this.contraint = contraint;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext cxt) {

        List<String> contentTypes = Arrays.asList(contraint.value());

        if (contentTypes.contains(file.getContentType())) {
            return true;
        }

        return false;
    }
}
