package code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JavaCodeUtilsTest {

    @Test
    public void parseMethodSignature() throws Exception{
        /*ObjectMapper objectMapper = new ObjectMapper();
        String measureIndexPath = "C:\\Users\\10146\\Downloads\\test\\test\\detect\\result\\MeasureIndex.csv";
        File measureIndexFile = new File(measureIndexPath);
        BufferedReader reader = new BufferedReader(new FileReader(measureIndexFile));
        String line = null;
        List<MethodSignature> signatures = new ArrayList<>();
        while ((line = reader.readLine()) != null){
            String[] tmp = line.split(",");
            MethodSignature methodSignature = JavaCodeUtils.parseMethodSignature(tmp[1], Integer.parseInt(tmp[2]));
            signatures.add(methodSignature);
        }
        reader.close();
        System.out.println(signatures.size());
        FileUtils.writeStringToFile(new File("signatures.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(signatures));
        */
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "SACloneDetector.jar", "d:\\GitRepo");
        processBuilder.directory(new File("C:\\Users\\10146\\Downloads\\test\\test\\detect"));
        Process process = processBuilder.start();
        Scanner scanner = new Scanner(process.getInputStream());
        while (scanner.hasNext()){
            System.out.println(scanner.nextLine());
        }
        process.waitFor();
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
            MethodBody methodBody = JavaCodeUtils.parseMethodBody(tmp[1], Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]));
            bodies.add(methodBody);
        }
        reader.close();
        MethodBody methodBody3 = bodies.get(3);
        MethodBody methodBody1244 = bodies.get(3);
        System.out.println(methodBody3.getIdentifiers().equals(methodBody1244.getIdentifiers()));
        FileUtils.writeStringToFile(new File("bodies.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodies));


    }
}