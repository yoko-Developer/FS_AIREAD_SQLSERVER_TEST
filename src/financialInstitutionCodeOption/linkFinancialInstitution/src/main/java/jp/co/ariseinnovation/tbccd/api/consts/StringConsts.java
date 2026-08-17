package jp.co.ariseinnovation.tbccd.api.consts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.Transliterator;

import jp.co.ariseinnovation.tbccd.api.config.AppSetting;

public final class StringConsts {
    private final static Logger log = LoggerFactory.getLogger(StringConsts.class);
    private final static Map<String, String> removeRepalceNames;
    private final static Map<String, String> legalPersonalityMap;

    /** ひらがな／カナの小文字⇒大文字変換 */
    private final static String LOWER_CHARS_JPN = "ぁぃぅぇぉゃゅょっァィゥェォャュョッｧｨｩｪｫｬｭｮｯ";
    private final static String UPPER_CHARS_JPN = "あいうえおやゆよつアイウエオヤユヨツｱｲｳｴｵﾔﾕﾖﾂ";
    private final static Map<Character, Character> UPPER_MAP_JPN = new HashMap<>();
    private final static List<String> NoukyoWordList = List.of("農協");

    public final static String toUpperCaseJpn(String src) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(src)) {
            src.chars().mapToObj(c -> (char) c).forEach(c -> {
                Character conv = UPPER_MAP_JPN.get(c);
                if (conv == null) {
                    sb.append(c);
                } else {
                    sb.append(conv);
                }
            });
        }
        return sb.toString();
    }

    /**
     * 特定文字列置換
     * 
     * @param src
     * @return
     */
    public final static String halfToFull(String src) {
        Transliterator transliterator = Transliterator.getInstance("Halfwidth-Fullwidth");
        String result = transliterator.transliterate(src);
        return result;
    }

    /**
     * 特定文字列置換
     * 
     * @param name
     * @return
     */
    public final static String replaceBankNameString(String name) {
        var ret = name;
        // 特定文字列置換
        for (var val : removeRepalceNames.entrySet()) {
            ret = ret.replaceAll(val.getKey(), val.getValue());
        }
        return ret;
    }

    /**
     * 略称の変換
     * 
     * @param name
     * @return
     */
    public final static String replaceAbbrName(String name) {
        var ret = name;
        // 略称の変換
        for (var val : legalPersonalityMap.entrySet()) {
            ret = ret.replace(val.getKey(), val.getValue());
        }
        return ret;
    }

    /**
     * 法人判定
     * 
     * @param name
     * @return
     */
    public final static boolean isCorporate(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        String normalizedName = replaceAbbrName(name);
        for (var val : legalPersonalityMap.values()) {
            if (normalizedName.contains(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 農協の支店名の削除
     * 
     * @param name
     * @return
     */
    public final static String replaceNoukyoBranchString(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }

        for (var word : NoukyoWordList) {

            // "農協"から後続の文字列を置換する
            var ret = name.replaceAll(word + ".*", word);
            if (!ret.equals(name)) {
                // // 置換された
                // if (ret.equals(word)) {
                //     // "農協"に置換された場合は置換前の文字列を返却する
                //     return name;
                // }
                return ret;
            }
        }

        return name;
    }

    static {
        removeRepalceNames = new HashMap<>();
        removeRepalceNames.put("㈱", "");
        removeRepalceNames.put("（株）", "");
        removeRepalceNames.put("株式会社", "");
        removeRepalceNames.put("／.*", "");
        removeRepalceNames.put("・.*", "");
        removeRepalceNames.put("銀行.*", "");
        removeRepalceNames.put("公庫.*", "公庫");
        removeRepalceNames.put("信用金庫.*", "信金");
        removeRepalceNames.put("労働金庫.*", "労金");
        removeRepalceNames.put("信用組合.*", "信組");
        removeRepalceNames.put("信用農業.*", "信農");
        removeRepalceNames.put("農業協同.*", "農協");
        removeRepalceNames.put("漁業協同.*", "漁協");
        removeRepalceNames.put("商工中金.*", "商工中金");
        removeRepalceNames.put("商工組合.*", "商工中金");

        char[] lowerChars = LOWER_CHARS_JPN.toCharArray();
        char[] upperChars = UPPER_CHARS_JPN.toCharArray();
        for (int i = 0; i < lowerChars.length; i++) {
            UPPER_MAP_JPN.put(lowerChars[i], upperChars[i]);
        }

        legalPersonalityMap = new HashMap<>();
        legalPersonalityMap.put("（株）", "株式会社");
        legalPersonalityMap.put("㈱", "株式会社");
        legalPersonalityMap.put("（有）", "有限会社");
        legalPersonalityMap.put("㈲", "有限会社");
        legalPersonalityMap.put("（名）", "合名会社");
        legalPersonalityMap.put("（資）", "合資会社");
        legalPersonalityMap.put("（同）", "合同会社");
        legalPersonalityMap.put("（財）", "財団法人");
        legalPersonalityMap.put("（一財）", "一般財団法人");
        legalPersonalityMap.put("（公財）", "公益財団法人");
        legalPersonalityMap.put("（社）", "社団法人");
        legalPersonalityMap.put("（一社）", "一般社団法人");
        legalPersonalityMap.put("（公社）", "公益社団法人");
        legalPersonalityMap.put("（宗）", "宗教法人");
        legalPersonalityMap.put("（学）", "学校法人");
        legalPersonalityMap.put("（福）", "社会福祉法人");
        legalPersonalityMap.put("（相）", "相互会社");
        legalPersonalityMap.put("（特非）", "特定非営利活動法人");
        legalPersonalityMap.put("（独）", "独立行政法人");
        legalPersonalityMap.put("（地独）", "地方独立行政法人");
        legalPersonalityMap.put("（弁）", "弁護士法人");
        legalPersonalityMap.put("（行）", "行政書士法人");
        legalPersonalityMap.put("（司）", "司法書士法人");
        legalPersonalityMap.put("（税）", "税理士法人");
        legalPersonalityMap.put("（営）", "営業所");
        legalPersonalityMap.put("（出）", "出張所");
        legalPersonalityMap.put("（連）", "連合会");
        legalPersonalityMap.put("（共済）", "共済組合");
        legalPersonalityMap.put("（協組）", "協同組合");
        legalPersonalityMap.put("（生命）", "生命保険");
        legalPersonalityMap.put("（海上）", "海上火災保険");
        legalPersonalityMap.put("（火災）", "火災海上保険");
        legalPersonalityMap.put("（健保）", "健康保険組合");
        legalPersonalityMap.put("（国保）", "国民健康保険組合");
        legalPersonalityMap.put("（国保連）", "国民健康保険団体連合会");
        legalPersonalityMap.put("（社保）", "社会保険診療報酬支払基金");
        legalPersonalityMap.put("（厚年）", "厚生年金基金");
        legalPersonalityMap.put("（従組）", "従業員組合");
        legalPersonalityMap.put("（労）組", "労働組合");
        legalPersonalityMap.put("（生協）", "生活協同組合");
        legalPersonalityMap.put("（食販協）", "食糧販売協同組合");
        legalPersonalityMap.put("（国共連）", "国家公務員共済組合連合会");
        legalPersonalityMap.put("（農協連）", "農業協同組合連合会");
        legalPersonalityMap.put("（経済連）", "経済農業協同組合連合会");
        legalPersonalityMap.put("（共済連）", "共済農業協同組合連合会");
        legalPersonalityMap.put("（漁協）", "漁業協同組合");
        legalPersonalityMap.put("（漁連）", "漁業協同組合連合会");
        legalPersonalityMap.put("（職安）", "公共職業安定所");
        legalPersonalityMap.put("（社協）", "社会福祉協議会");
        legalPersonalityMap.put("（特養）", "特別養護老人ホーム");
        legalPersonalityMap.put("（責）", "有限責任事業組合");
        legalPersonalityMap.put("（協）", "協同組合");
    }
}
