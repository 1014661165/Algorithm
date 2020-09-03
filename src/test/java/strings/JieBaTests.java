package strings;

import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.io.StringReader;

public class JieBaTests {

    public static final Logger logger = LoggerFactory.getLogger(JieBaTests.class);


    @Test
    public void cutSentence()throws Exception{
        logger.info("123");
    }

    public static void main(String[] args) {
        logger.info("123");
    }
}
