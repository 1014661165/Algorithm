package code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * JavaScript代码工具类
 */
public class JavaScriptCodeUtils {

    private static final String JAVA_SCRIPT_SINGLE_LINE_COMMENT_PATTERN = "//[\\s\\S]*?\n";
    private static final String JAVA_SCRIPT_MULTI_LINE_COMMENT_PATTERN = "/\\*[\\s\\S]*?\\*/";
    private static final String JAVA_SCRIPT_STRING_PATTERN = "\"[\\s\\S]*?\"";
    private static final String JAVA_SCRIPT_STRING_PATTERN2 = "'[\\s\\S]*?'";

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

        try{
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

            //跳过空格
            index = skipWhitespace(code, --index);
            char c = code.charAt(index);
            if (c != ')'){
                return null;
            }

            //提取参数列表
            StringBuilder builder = new StringBuilder();
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
            String str = builder.deleteCharAt(builder.length()-1).reverse().toString().trim();
            if(!str.isEmpty()){
                str = str.replaceAll("[\\s]+", "");
                String[] params = str.split(",");
                int pi = 0;
                int angleBrackets = 0;
                char[] brackets = new char[]{'[', ']'};
                String lastParam = "";
                while (pi < params.length){
                    String param = params[pi];
                    int[] cnt = count(param, brackets);
                    int sum = cnt[0] - cnt[1];
                    if (sum + angleBrackets == 0){
                        if (!lastParam.isEmpty()){
                            param = lastParam + "," + param;
                        }

                        String paramType = "";
                        String paramName = param;
                        if (paramName.contains("=")){
                            paramName = paramName.substring(0, paramName.indexOf("="));
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

            //提取方法名
            builder.delete(0, builder.length());
            index = skipWhitespace(code, --index);
            c = code.charAt(index);
            while (Character.isLetterOrDigit(c) || c == '_'){
                builder.append(c);
                index--;
                if (index < 0){
                    return null;
                }
                c = code.charAt(index);
            }
            str = builder.reverse().toString().trim();
            if ("function".equals(str)){
                methodSignature.setMethodName("anonymous");
            }else{
                methodSignature.setMethodName(str);
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
            code = code.replaceAll(JAVA_SCRIPT_SINGLE_LINE_COMMENT_PATTERN, "");
            code = code.replaceAll(JAVA_SCRIPT_MULTI_LINE_COMMENT_PATTERN, "");
            code = code.replaceAll(JAVA_SCRIPT_STRING_PATTERN, "");
            code = code.replaceAll(JAVA_SCRIPT_STRING_PATTERN2, "");

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
