package test.edu.iselab.cloner.util;

import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.iselab.cloner.model.Project;
import edu.iselab.cloner.util.GitUtils;

public class TestGitUtils {

    @Test
    public void shouldThrowNullPointExceptionWhenProjectIsNull() {

//        Assertions.assertThrows(NullPointerException.class, () -> {
//            GitUtils.clone(null, Paths.get(""));
//        });
    }
    
    @Test
    public void shouldThrowNullPointExceptionWhenPathIsNull() {

//        Assertions.assertThrows(NullPointerException.class, () -> {
//            GitUtils.clone(new Project("", "", ""), null);
//        });
    }
}
