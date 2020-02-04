package strings;

import java.util.*;

public class StringUtil {

    /**
     * 计算token串的余弦相似度
     * @param tokensX
     * @param tokensY
     * @return
     */
    public static float cosineSimilarity(List<Byte> tokensX, List<Byte> tokensY){
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
        return (float) (Math.sqrt(xy)/(Math.sqrt(x) * Math.sqrt(y)));
    }
}
