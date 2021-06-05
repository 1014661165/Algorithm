package code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Golang代码工具类
 */
public class GolangCodeUtils {

    private static final String GOLANG_SINGLE_LINE_COMMENT_PATTERN = "//[\\s\\S]*?\n";
    private static final String GOLANG_MULTI_LINE_COMMENT_PATTERN = "/\\*[\\s\\S]*?\\*/";
    private static final String GOLANG_STRING_PATTERN = "\"[\\s\\S]*?\"";

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

            //以func为锚点向后扫描
            String code = String.join("\n", lines);
            int index = code.lastIndexOf("func ");
            if (index == -1){
                return null;
            }

            index += 4;
            index = skipWhitespace(code, index);
            if (index == code.length()){
                return null;
            }

            //跳过成员函数声明
            char c = code.charAt(index);
            if (c == '('){
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
                }
                index++;
                index = skipWhitespace(code, index);
            }

            //拼接函数名
            c = code.charAt(index);
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
                str = str.replaceAll("[\\s]+", " ");
                String[] params = str.split(",");
                List<Integer> lastParamIndices = new ArrayList<>();
                for (int i=0; i<params.length; i++){
                    String param = params[i].trim();
                    if (param.contains(" ")){
                        String paramName = param.substring(0, param.lastIndexOf(" "));
                        String paramType = param.substring(param.lastIndexOf(" ")+1);
                        methodSignature.getParams().add(new MethodSignature.MethodParam(paramType, paramName));
                        if (lastParamIndices.size() != 0){
                            lastParamIndices.stream().forEach(s->methodSignature.getParams().get(s).setParamType(paramType));
                            lastParamIndices.clear();
                        }
                    }else{
                        methodSignature.getParams().add(new MethodSignature.MethodParam("", param));
                        lastParamIndices.add(i);
                    }
                }
            }

            //提取返回类型
            builder = new StringBuilder();
            while (c != '{'){
                index++;
                if (index >= code.length()){
                    return null;
                }
                c = code.charAt(index);
                if (c != '{'){
                    builder.append(c);
                }
            }
            str = builder.toString().trim();
            if (!str.isEmpty()){
                methodSignature.setReturnType(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return methodSignature;
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
            code = code.replaceAll(GOLANG_SINGLE_LINE_COMMENT_PATTERN, "");
            code = code.replaceAll(GOLANG_MULTI_LINE_COMMENT_PATTERN, "");
            code = code.replaceAll(GOLANG_STRING_PATTERN, "");

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
            res++;
            if (res >= code.length()){
                return code.length();
            }
            c = code.charAt(res);
        }
        return res;
    }
}
