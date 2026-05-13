package org.ofdrw.sign;

import java.util.regex.Pattern;

/**
 * signature ID提供者
 * <p>
 * 开发者可以根据实际配置signature ID的格式
 *
 * @author Quan Guanyu
 * @since 2020-08-24 20:12:35
 */
public interface SignIDProvider {


    /**
     * 根据标准推荐ID样式为
     * <p>
     * 'sNNN',NNN从1起。
     */
    Pattern IDPattern = Pattern.compile("s(\\d+)");

    /**
     * set current maximum signature ID value
     * <p>
     * implementor must parse this string and set the built-in counter
     *
     * @param maxSignId current maximum signature ID format string
     */
    void setCurrentMaxSignId(String maxSignId);

    /**
     * increment and get signature ID
     *
     * @return signature ID，形如：'s001'
     */
    String incrementAndGet();


    /**
     * 获取当前signature ID，不增长
     *
     * @return signature ID
     */
    String get();


    /**
     * parse the ID number of the electronic signature
     *
     * @param id ID string
     * @return ID number
     */
    int parse(String id);
}
