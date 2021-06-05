package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * C++代码工具类
 */
public class CppCodeUtils {

    private static final String CPP_SINGLE_LINE_COMMENT_PATTERN = "//[\\s\\S]*?\n";
    private static final String CPP_MULTI_LINE_COMMENT_PATTERN = "/\\*[\\s\\S]*?\\*/";
    private static final String CPP_STRING_PATTERN = "\"[\\s\\S]*?\"";

    /**
     * 解析函数签名
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

            //以左大括号为锚点，向前扫描
            String code = String.join("\n", lines);
            int index = code.lastIndexOf("{");
            if (index == -1){
                return null;
            }
            char c = code.charAt(index);

            //扫描左大括号到右小括号之间是否存在const
            StringBuilder builder = new StringBuilder();
            while (c != ')'){
                index--;
                if (index < 0){
                    return null;
                }
                c = code.charAt(index);
                if (c != ')'){
                    builder.append(c);
                }
            }
            String str = builder.reverse().toString().trim();
            if (!str.isEmpty()){
                if (!str.equals("const")){
                    return null;
                }
            }

            //从右小括号扫描到左小括号，提取参数列表
            builder.delete(0, builder.length());
            int rightParenNum = 1;
            while (rightParenNum != 0){
                index--;
                if (index < 0){
                    return null;
                }
                c = code.charAt(index);
                if (c == '('){
                    rightParenNum--;
                }else if (c == ')'){
                    rightParenNum++;
                }
                builder.append(c);
            }
            str = builder.deleteCharAt(builder.length()-1).reverse().toString().trim();
            if (!str.isEmpty()){
                str = str.replaceAll("[\\s]+", " ");
                if (!str.contains(" ")){
                    return null;
                }

                String[] params = str.split(",");
                int pi = 0;
                int angleBrackets = 0;
                char[] brackets = new char[]{'<', '>', '(', ')'};
                String lastParam = "";
                while (pi < params.length){
                    String param = params[pi].trim();
                    int[] cnt = count(param, brackets);
                    int sum = cnt[0] - cnt[1] + cnt[2] - cnt[3];
                    if (sum + angleBrackets == 0){
                        if (!lastParam.isEmpty()){
                            param = lastParam + "," + param;
                        }
                        if (param.lastIndexOf(" ") == -1){
                            pi++;
                            continue;
                        }

                        String paramType = "";
                        String paramName = "";
                        if (param.contains("(") && param.contains(")")){
                            //参数是闭包函数
                            paramType = param.substring(0, param.indexOf(" "));
                            paramName = param.substring(param.indexOf("(")+1, param.indexOf(")"));
                            paramName = paramName.replaceAll("\\*", "");
                        }else{
                            paramType = param.substring(0, param.lastIndexOf(" "));
                            paramName = param.substring(param.lastIndexOf(" ")+1);
                            if (paramName.contains("*")){
                                String tmp = paramType + paramName;
                                paramType = tmp.substring(0, tmp.lastIndexOf("*")+1);
                                paramName = tmp.substring(tmp.lastIndexOf("*")+1);
                            }
                        }
                        methodSignature.getParams().add(new MethodSignature.MethodParam(paramType, paramName));
                        lastParam = "";
                        angleBrackets = 0;
                    }else{
                        lastParam = (lastParam.isEmpty())? param: lastParam + "," + param;
                        angleBrackets += sum;
                    }
                    pi++;
                }
            }

            //提取方函数名
            builder.delete(0, builder.length());
            index = skipWhitespace(code, --index);
            c = code.charAt(index);
            while (!Character.isWhitespace(c)){
                builder.append(c);
                index--;
                if (index < 0){
                    return null;
                }
                c = code.charAt(index);
            }
            str = builder.reverse().toString().trim();
            methodSignature.setMethodName(str);

            //提取返回类型
            builder.delete(0, builder.length());
            index = skipWhitespace(code, index);
            c = code.charAt(index);
            while (!Character.isWhitespace(c)){
                builder.append(c);
                index--;
                if (index < 0){
                    return null;
                }
                c = code.charAt(index);
            }
            str = builder.reverse().toString().trim();
            if (str.equals("__inline")){
                builder.delete(0, builder.length());
                index = skipWhitespace(code, index);
                c = code.charAt(index);
                while (!Character.isWhitespace(c)){
                    builder.append(c);
                    index--;
                    if (index < 0){
                        return null;
                    }
                    c = code.charAt(index);
                }
                str = builder.reverse().toString().trim();

            }
            methodSignature.setReturnType(str);
            return methodSignature;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析方法体
     * @param path
     * @param startLine
     * @param endLine
     * @return
     */
    public static MethodBody parseMethodBody(String path, int startLine, int endLine){
        MethodBody methodBody = new MethodBody();
        methodBody.setPath(path);
        methodBody.setStartLine(startLine);
        methodBody.setEndLine(endLine);
        File file = new File(path);
        if (!file.exists()){
            return null;
        }
        try {
            Scanner scanner = new Scanner(file);
            List<String> lines = new ArrayList<>();
            int cnt = 0;
            while (scanner.hasNextLine()){
                cnt++;
                String line = scanner.nextLine();
                if (cnt >= startLine && cnt <= endLine){
                    lines.add(line);
                }else if (cnt > endLine){
                    break;
                }
            }
            scanner.close();

            String code = String.join("\n", lines);
            int leftBracketIndex = code.indexOf("{");
            int rightBracketIndex = code.lastIndexOf("}");
            if (leftBracketIndex == -1 || rightBracketIndex == -1){
                return null;
            }
            code = code.substring(leftBracketIndex+1, rightBracketIndex);
            code = code.replaceAll(CPP_SINGLE_LINE_COMMENT_PATTERN, "");
            code = code.replaceAll(CPP_MULTI_LINE_COMMENT_PATTERN, "");
            code = code.replaceAll(CPP_STRING_PATTERN, "");

            scanner = new Scanner(code);
            String pattern = "[A-Za-z_]+[A-Za-z0-9_]*";
            while (scanner.hasNextLine()){
                scanner.nextLine();
                String token = scanner.findInLine(pattern);
                while (token != null){
                    methodBody.getIdentifiers().add(token);
                    token = scanner.findInLine(pattern);
                }
            }
            scanner.close();
            return methodBody;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
            res--;
            if (res < 0){
                return 0;
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
