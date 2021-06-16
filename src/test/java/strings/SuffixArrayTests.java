package strings;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuffixArrayTests {

    private static SuffixArray suffixArray;
    private static List<Byte> tokens;

    @BeforeClass
    public static void init() throws IOException {
        suffixArray = new SuffixArray();
        tokens = new ArrayList<>();
        byte[] data = FileUtils.readFileToByteArray(new File("D:\\GitRepo\\SAGACloneDetector\\tokenData\\allTokenCsv0"));
        for(byte b: data){
            tokens.add(b);
        }
    }


    @Test
    public void testBuildSuffixArray()throws Exception{
        suffixArray.init(tokens);
        suffixArray.buildSuffixArray();
        int[] sa = suffixArray.getSa();
        BufferedWriter writer = new BufferedWriter(new FileWriter("sa.txt"));
        for (int num: sa){
            writer.write(String.format("%d,", num));
        }
        writer.close();
    }
}
