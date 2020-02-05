package strings;

import java.util.*;

public class StringUtil {

    /**
     * 计算token串的余弦相似度
     * @param tokensX
     * @param tokensY
     * @return
     */
    public static double cosineSimilarity(List<Byte> tokensX, List<Byte> tokensY){
        List<Byte> allTokens = new ArrayList<>();
        allTokens.addAll(tokensX);
        allTokens.addAll(tokensY);
        Set<Byte> tokenSet = new HashSet<>(allTokens);
        Map<Byte, Integer> tokenMapX = new HashMap<>();
        Map<Byte, Integer> tokenMapY = new HashMap<>();
        for (Byte b: tokensX) {
            tokenMapX.put(b, tokenMapX.getOrDefault(b, 0) + 1);
        }
        for (Byte b: tokensY) {
            tokenMapY.put(b, tokenMapY.getOrDefault(b, 0) + 1);
        }
        List<Integer> vecX = new ArrayList<>();
        List<Integer> vecY = new ArrayList<>();
        for (Byte b: tokenSet) {
            vecX.add(tokenMapX.getOrDefault(b, 0));
            vecY.add(tokenMapY.getOrDefault(b, 0));
        }

        long x=0,y=0,xy=0;
        for (int i=0; i<tokenSet.size(); i++) {
            xy += vecX.get(i) * vecY.get(i);
            x += vecX.get(i) * vecX.get(i);
            y += vecY.get(i) * vecY.get(i);
        }
        return xy/(Math.sqrt(x) * Math.sqrt(y));
    }

    /**
     * 计算字符串编辑距离
     * @param tokensX
     * @param tokensY
     * @return
     */
    public static int editDistance(byte[] tokensX, byte[] tokensY){
        int[][] matrix = new int[tokensX.length+1][tokensY.length+1];
        for (int i=0; i<matrix[0].length; i++) {
            matrix[0][i] =  i;
        }
        for (int i=0; i<matrix.length; i++) {
            matrix[i][0] =  i;
        }
        for (int i=1; i<matrix.length; i++) {
            for (int j=1; j<matrix[0].length; j++) {
                Byte b1 = tokensX[i - 1];
                Byte b2 = tokensY[j - 1];
                int leftTop = b1.equals(b2)? matrix[i-1][j-1]: matrix[i-1][j-1] + 1;
                int left = matrix[i][j-1] + 1;
                int top = matrix[i - 1][j] + 1;
                matrix[i][j] = Math.min(leftTop, Math.min(left, top));
            }
        }
        return matrix[matrix.length - 1][matrix[0].length - 1];
    }
}
