package edu.iselab.gitcloner.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Charsets;

public class FileUtils {

    public static void createFolderIfNotExists(Path path) {

        checkNotNull(path, "path should not be null");
        
        if (!Files.exists(path)) {

            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static String readContentAsString(MultipartFile file) {

        checkNotNull(file, "file should not be null");

        String content = null;

        try {
            content = new String(file.getBytes(), Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return content;
    }
    
    public static void delete(Path directory) {
        
        checkNotNull(directory, "directory should not be null");
        checkArgument(!StringUtils.isBlank(directory.toString()), "directory should not be blank");
        
        if (!Files.exists(directory)) {
            return;
        }
        
        try {
            
            Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
