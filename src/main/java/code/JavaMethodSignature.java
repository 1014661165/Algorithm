package code;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JavaMethodSignature {

    private String returnType;
    private String methodName;
    private List<MethodParam> params = new ArrayList<>();
    private List<String> exceptions = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class MethodParam{
        private String paramType;
        private String paramName;
    }
}
