package code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PythonCodeUtilsTest {

    @Test
    public void parseMethodSignature()throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String measureIndexPath = "C:\\Users\\10146\\Downloads\\test\\test\\detect\\result\\MeasureIndex.csv";
        File measureIndexFile = new File(measureIndexPath);
        BufferedReader reader = new BufferedReader(new FileReader(measureIndexFile));
        String line = null;
        List<MethodSignature> signatures = new ArrayList<>();
        while ((line = reader.readLine()) != null){
            String[] tmp = line.split(",");
            MethodSignature methodSignature = PythonCodeUtils.parseMethodSignature(tmp[1], Integer.parseInt(tmp[2]));
            signatures.add(methodSignature);
        }
        reader.close();
        System.out.println(signatures.size());
        FileUtils.writeStringToFile(new File("signatures.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(signatures));

    }

    @Test
    public void parseMethodBody() throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();
        String measureIndexPath = "C:\\Users\\10146\\Downloads\\test\\test\\detect\\result\\MeasureIndex.csv";
        File measureIndexFile = new File(measureIndexPath);
        BufferedReader reader = new BufferedReader(new FileReader(measureIndexFile));
        String line = null;
        List<MethodBody> bodies = new ArrayList<>();
        while ((line = reader.readLine()) != null){
            String[] tmp = line.split(",");
            MethodBody methodBody = PythonCodeUtils.parseMethodBody(tmp[1], Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]));
            bodies.add(methodBody);
        }
        reader.close();
        FileUtils.writeStringToFile(new File("bodies.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodies));

    }
}