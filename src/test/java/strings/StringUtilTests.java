package strings;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StringUtilTests {

    @Test
    public void testCosineSimilarity() throws IOException {
        List<Byte> tokenX = new ArrayList<>();
        for (int i=1; i<=7; i++){
            tokenX.add((byte)i);
        }
        List<Byte> tokenY = new ArrayList<>();
        for (int i=1; i<=3; i++){
            tokenY.add((byte)i);
        }
        for (int i=8; i<=11; i++){
            tokenY.add((byte)i);
        }

        double similarity = StringUtil.cosineSimilarity(tokenX, tokenY);
        System.out.println(similarity);
    }
}
