package code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Python代码工具类
 */
public class PythonCodeUtils {

    /**
     * 解析方法签名
     * @param path
     * @param startLine
     * @return
     */
    public static MethodSignature parseMethodSignature(String path, int startLine){
        MethodSignature methodSignature = new MethodSignature();
        methodSignature.setPath(path);
        methodSignature.setStartLine(startLine);
        File file = new File(path);
        if (!file.exists()){
            return null;
        }

        try {
            //读取代码
            Scanner scanner = new Scanner(file);
            List<String> lines = new ArrayList<>();
            int lineCount = 0;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                lineCount++;
                if (lineCount > startLine){
                    break;
                }
                lines.add(line);
            }
            scanner.close();





        }catch (Exception e){
            e.printStackTrace();
        }
        return methodSignature;
    }

}
