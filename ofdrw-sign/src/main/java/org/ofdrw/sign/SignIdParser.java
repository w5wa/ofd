package org.ofdrw.sign;

import java.util.regex.Matcher;

/**
 * signature ID解析器
 *
 * @author Quan Guanyu
 * @since 2020-10-13 19:38:50
 */
public class SignIdParser {

    /**
     * parse the ID number of the electronic signature
     * <p>
     * 支持标准推荐样式s'NNN'、sN、N三种类型signature ID的解析
     *
     * @param id ID string
     * @return ID number
     */
    public static int parseIndex(String id) {
        Matcher m = SignIDProvider.IDPattern.matcher(id);
        if (m.find()) {
            String idNumStr = m.group(1);
            return Integer.parseInt(idNumStr);
        } else {
            return Integer.parseInt(id);
        }
    }
}
