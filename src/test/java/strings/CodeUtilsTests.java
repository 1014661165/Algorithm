package strings;

import org.junit.Test;

public class CodeUtilsTests {


    @Test
    public void testIsSimilarCode(){
        String code1 = "List<Object> tokens1 = lexer(code1, tokenize);\n" +
                "            List<Object> tokens2 = lexer(code2, tokenize);\n" +
                "            double similarity = cosineSimilarity(tokens1, tokens2);";
        String code2 = "List<Object> tokens1 = lexer(code1, tokenize);\n" +
                "            List<Object> tokens2 = lexer(code2, tokenize);\n" +
                "            double similarity = cosineSimilarity(tokens1);";
        float res = CodeUtils.isSimilarCode(code1, code2);
        System.out.println(res);
    }
}
