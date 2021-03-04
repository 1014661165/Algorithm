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
            int brackets = 0;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                int[] cnt = count(line, '(', ')');
                int sum = cnt[0] - cnt[1];
                brackets += sum;

                lineCount++;
                if (lineCount > startLine && sum + brackets == 0){
                    break;
                }
                lines.add(line);
            }
            scanner.close();

            //以def为锚点向后扫描
            String code = String.join("\n", lines);
            int index = code.lastIndexOf("def ");
            if (index == -1){
                return null;
            }

            index += 3;
            index = skipWhitespace(code, index);
            if (index == code.length()){
                return null;
            }

            //拼接函数名
            char c = code.charAt(index);
            StringBuilder builder = new StringBuilder();
            while (Character.isLetterOrDigit(c) || c == '_'){
                builder.append(c);
                index++;
                if (index >= code.length()){
                    return null;
                }
                c = code.charAt(index);
            }
            methodSignature.setMethodName(builder.toString());

            //提取参数列表
            index = skipWhitespace(code, index);
            c = code.charAt(index);
            if (c != '('){
                return null;
            }
            builder = new StringBuilder();
            int leftParen = 1;
            while (leftParen != 0){
                index++;
                if (index >= code.length()){
                    return null;
                }
                c = code.charAt(index);
                if (c == '('){
                    leftParen++;
                }else if (c == ')'){
                    leftParen--;
                }
                builder.append(c);
            }
            String str = builder.deleteCharAt(builder.length()-1).toString().trim();
            if (!str.isEmpty()){
                str = str.replaceAll("[\\s]+", "");
                String[] params = str.split(",");
                for (String param: params){
                    if (param.contains(":")){
                        //带参数类型
                        String[] tmp = param.split(":");
                        methodSignature.getParams().add(new MethodSignature.MethodParam(tmp[1], tmp[0]));
                    }else if (param.contains("=")){
                        //带默认值
                        String[] tmp = param.split("=");
                        methodSignature.getParams().add(new MethodSignature.MethodParam("", tmp[0]));
                    }else{
                        methodSignature.getParams().add(new MethodSignature.MethodParam("", param));
                    }
                }
            }

            //从)扫描到:,提取返回类型
            builder = new StringBuilder();
            while (c != ':'){
                index++;
                if (index >= code.length()){
                    return null;
                }
                c = code.charAt(index);
                if (c != ':'){
                    builder.append(c);
                }
            }
            str = builder.toString().trim();
            if (!str.isEmpty()){
                String returnType = str.substring(str.indexOf("->")+2);
                methodSignature.setReturnType(returnType);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return methodSignature;
    }

    /**
     * 跳过空格
     * @param code
     * @param index
     * @return
     */
    private static int skipWhitespace(String code, int index){
        int res = index;
        char c = code.charAt(res);
        while (Character.isWhitespace(c)){
            res++;
            if (res >= code.length()){
                return code.length();
            }
            c = code.charAt(res);
        }
        return res;
    }

    /**
     * 统计字符串s中C字符的个数
     * @param s
     * @param chars
     * @return
     */
    private static int[] count(String s, char ...chars){
        int[] cnt = new int[chars.length];
        for (char sc: s.toCharArray()){
            for (int i=0; i<chars.length; i++){
                if (sc == chars[i]){
                    cnt[i]++;
                    break;
                }
            }
        }
        return cnt;
    }
}
