package strings;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SuffixArrayTests {

    private static SuffixArray suffixArray;
    private static List<Byte> tokens;

    @BeforeClass
    public static void init(){
        suffixArray = new SuffixArray();
        tokens = new ArrayList<>();
        for (int i=0; i<10000; i++) {
            int num = 10 - i%10;
            tokens.add((byte)num);
        }
    }


    @Test
    public void testBuildSuffixArray()throws Exception{
        suffixArray.init(tokens);
        long start = System.currentTimeMillis();
        List<SuffixArray.Result> results = suffixArray.process();
        long end = System.currentTimeMillis();
        System.out.printf("%d ms\n", (end - start));

        /*for (SuffixArray.Result result: results){
            System.out.printf("%d %d %d\n", result.firstIndex, result.secondIndex, result.length);
            System.out.println(tokens.subList(result.firstIndex, tokens.size()));
            System.out.println(tokens.subList(result.secondIndex, tokens.size()));
        }*/
    }
}
