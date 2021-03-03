package code;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MethodBody {

    private String path;
    private int startLine;
    private int endLine;
    private List<String> identifiers = new ArrayList<>();
}
