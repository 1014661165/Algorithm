package strings;

import java.util.*;
import java.util.function.IntUnaryOperator;

/**
 * 后缀数组工具类
 */
@SuppressWarnings("Duplicates")
public class SuffixArray {
    private List<Byte> tokens;
    private int[] sa;
    private int[] height;

    /**
     * 初始化token列表
     * @param tokens
     */
    public void init(List<Byte> tokens){
        this.tokens = tokens;
        sa = new int[tokens.size()];
        height = new int[tokens.size()];
    }

    /**
     * 构建后缀数组
     */
    public void buildSuffixArray(){
        //初始化sa
        for (int i=0; i<sa.length; i++){
            sa[i] = i;
        }
        for(int i=0; i<tokens.size()-1; i++) {
            for (int j=i+1; j<tokens.size(); j++){
                List<Byte> suffix1 = tokens.subList(sa[i], tokens.size());
                List<Byte> suffix2 = tokens.subList(sa[j], tokens.size());
                int size = Math.min(suffix1.size(), suffix2.size());
                boolean result = suffix1.size() < suffix2.size();
                for (int m=0; m<size; m++){
                    if (suffix1.get(m) < suffix2.get(m)){
                        result = true;
                        break;
                    }else if (suffix1.get(m) > suffix2.get(m)){
                        result = false;
                        break;
                    }
                }
                if (!result){
                    int tmp = sa[i];
                    sa[i] = sa[j];
                    sa[j] = tmp;
                }
            }
        }
    }

    /**
     * 计算高度数组
     */
    public void calculateHeight(){
        for (int i=1; i<sa.length; i++){
            List<Byte> pre = tokens.subList(sa[i - 1], tokens.size());
            List<Byte> cur = tokens.subList(sa[i], tokens.size());
            int cnt = 0;
            int size = Math.min(pre.size(), cur.size());
            for (int j=0; j<size; j++){
                if (!pre.get(j).equals(cur.get(j))){
                    break;
                }
                cnt++;
            }
            height[i] = cnt;
        }
    }

    /**
     * 获取检测结果
     * @return
     */
    public List<Integer> getResult(){
        List<Integer> results = new ArrayList<>();
        for (int i=1; i<height.length; i++){
            results.add(sa[i-1]);
            results.add(sa[i]);
            results.add(height[i]);
        }
        return results;
    }

    public List<Byte> getTokens() {
        return tokens;
    }

    public int[] getSa() {
        return sa;
    }

    public int[] getHeight() {
        return height;
    }
}
