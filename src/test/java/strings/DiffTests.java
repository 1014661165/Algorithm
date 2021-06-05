package strings;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DiffTests {


    @Test
    public void testDiff() throws IOException {
        char c1 = ' ';
        char c2 = '\t';
        char c3 = '\r';
        char c4 = '\n';
        char c5 = 'a';
        System.out.println(Character.isWhitespace(c1));
        System.out.println(Character.isWhitespace(c2));
        System.out.println(Character.isWhitespace(c3));
        System.out.println(Character.isWhitespace(c4));
        System.out.println(Character.isWhitespace(c5));
    }
}
