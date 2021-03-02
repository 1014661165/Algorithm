package code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Java代码工具类
 */
public class JavaCodeUtils {

    /**
     * 解析方法签名
     * @param path
     * @param methodStartLine
     * @return
     */
    public static JavaMethodSignature parseMethodSignature(String  path, int methodStartLine ) throws IOException {
        JavaMethodSignature javaMethodSignature = new JavaMethodSignature();
        File file = new File(path);
        if (!file.exists()){
            return null;
        }

        //读取代码
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();
        String line = null;
        int lineCount = 0;
        while ((line = reader.readLine()) != null){
            lineCount++;
            if (lineCount > methodStartLine){
                break;
            }
            lines.add(line);
        }
        reader.close();

        //以左大括号为锚点，向前扫描
        String code = String.join("\n", lines);
        int index = code.lastIndexOf("{");
        if (index == -1){
            return null;
        }
        char c = code.charAt(index);

        //扫描左大括号到右小括号之间是否抛出异常
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
        if (str.contains("=")){
            return null;
        }

        if (!str.isEmpty() && str.contains("throws")){
            str = str.replaceAll("throws", "");
            str = str.replaceAll("[\\s]", "");
            String[] exceptions = str.split(",");
            javaMethodSignature.setExceptions(Arrays.asList(exceptions));
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
                        continue;
                    }

                    String paramType = param.substring(0, param.lastIndexOf(" "));
                    if (paramType.contains("@")){
                        paramType = paramType.substring(paramType.lastIndexOf(" ")+1);
                    }

                    String paramName = param.substring(param.lastIndexOf(" ")+1);
                    javaMethodSignature.getParams().add(new JavaMethodSignature.MethodParam(paramType, paramName));
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
        while (!Character.isWhitespace(c)){
            builder.append(c);
            index--;
            if (index < 0){
                return null;
            }
            c = code.charAt(index);
        }
        str = builder.reverse().toString().trim();
        javaMethodSignature.setMethodName(str);

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
        javaMethodSignature.setReturnType(str);


        return javaMethodSignature;
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