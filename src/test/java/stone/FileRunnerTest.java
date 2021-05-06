package stone;

import org.junit.jupiter.api.Test;
import stone.exception.ParseException;
import stone.exception.TypeException;

import java.io.FileNotFoundException;

/**
 * @author XUQING
 * @date 2021/5/6
 */
public class FileRunnerTest {
    @Test
    public void fileRunnerTest() throws FileNotFoundException, TypeException, ParseException {
        FileRunner.runForFile(getClass().getResource("/fileRunnerTest.stone").getFile());
    }
}
