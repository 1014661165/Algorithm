package strings;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DiffTests {


    @Test
    public void testDiff() throws IOException {
        List<String> lines1 = Files.readAllLines(Paths.get("resources/a.txt"));
        List<String> lines2 = Files.readAllLines(Paths.get("resources/b.txt"));
        List<String> patch = DiffUtils.getPatch(lines1, lines2);
        System.out.println(patch);
    }
}
