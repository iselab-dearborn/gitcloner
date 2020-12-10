package edu.iselab.gitcloner.persistence.converter;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.AttributeConverter;

public class PathConverter implements AttributeConverter<Path, String> {

    @Override
    public String convertToDatabaseColumn(Path path) {
        return path.toString();
    }

    @Override
    public Path convertToEntityAttribute(String str) {
        return Paths.get(str);
    }
}
