package strings;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.BeforeClass;
import org.junit.Test;

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
        for (int i=0; i<8; i++){
            tokens.add((byte)(i%4));
        }
    }


    @Test
    public void testBuildSuffixArray()throws Exception{
        long start = System.currentTimeMillis();
        suffixArray.init(tokens);
        List<SuffixArray.Result> results = suffixArray.process();
        long end = System.currentTimeMillis();
        System.out.printf("result size:%d time cost:%.1f s\n",results.size()*3 ,(end - start)/1e3);
    }
}
