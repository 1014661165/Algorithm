package strings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Test;

import java.io.IOException;
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

        double similarity = StringUtils.cosineSimilarity(tokenX, tokenY);
        System.out.println(similarity);
    }

    @Test
    public void testEditDistance()throws IOException{
        String a = "coffee";
        String b = "cafe";
        int dist = StringUtils.editDistance(a.getBytes(), b.getBytes());

        System.out.printf("%d, %.2f\n", dist, 1-dist*1f/(a.length() + b.length()));
    }

    @Test
    public void testXmlAndJson() throws Exception{

        int[] nums = new int[]{1, 2, 3, 4, 5};
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nums);
        System.out.println(json.length());

        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nums);
        System.out.println(xml.length());
    }
}
