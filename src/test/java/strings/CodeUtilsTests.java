package strings;

import org.junit.Test;

public class CodeUtilsTests {


    @Test
    public void testIsSimilarCode(){
        String code1 = "List<Object> tokens1 = lexer(code1, tokenize);\n" +
                "            List<Object> tokens2 = lexer(code2, tokenize);\n" +
                "            double similarity = cosineSimilarity(tokens1, tokens2);\n" +
                "            return similarity >= threshold;";
        String code2 = "List<Object> tokens1 = lexer(code1, tokenize);\n" +
                "            List<Object> tokens2 = lexer(code2, tokenize);\n" +
                "            double similarity = cosineSimilarity(tokens1, tokens2);\n";
        boolean res = CodeUtils2.isSimilarCode(code1, code2, 0.7d);
        System.out.println(res);
    }
}
