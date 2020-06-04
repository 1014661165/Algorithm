package strings;

import java.util.*;

/**
 * 后缀数组工具类
 */
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
    private void buildSuffixArray(){
        //获取所有后缀
        List<List<Byte>> tokensList = new ArrayList<>();
        for (int i=0; i<tokens.size(); i++){
            tokensList.add(tokens.subList(i, tokens.size()));
        }

        //对所有后缀排序
        Collections.sort(tokensList, new Comparator<List<Byte>>() {
            @Override
            public int compare(List<Byte> o1, List<Byte> o2) {
                int size = Math.min(o1.size(), o2.size());
                int result = (o1.size() < o2.size())? -1: 1;

                for (int i=0; i<size; i++){
                    if (o1.get(i) < o2.get(i)){
                        result = -1;
                        break;
                    }else if (o1.get(i) > o2.get(i)){
                        result = 1;
                        break;
                    }
                }
                return result;
            }
        });

        //计算后缀数组
        for (int i=0; i<tokensList.size(); i++){
            List<Byte> suffix = tokensList.get(i);
            sa[i] = tokens.size() - suffix.size();
        }
        tokensList.clear();
    }

    /**
     * 计算高度数组
     */
    private void calculateHeight(){
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
    public List<Result> process(){
        buildSuffixArray();
        calculateHeight();
        List<Result> results = new ArrayList<>();
        for (int i=1; i<height.length; i++){
            Result result = new Result();
            result.firstIndex = sa[i - 1];
            result.secondIndex = sa[i];
            result.length = height[i];
            results.add(result);
        }
        return results;
    }

    /**
     * 后缀数组检测结果类
     */
    public static class Result{
        public int firstIndex;
        public int secondIndex;
        public int length;
    }

}
