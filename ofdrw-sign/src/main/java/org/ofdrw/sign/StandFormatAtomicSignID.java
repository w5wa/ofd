package org.ofdrw.sign;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自增的signature ID
 *
 * @author Quan Guanyu
 * @since 2020-04-17 04:20:15
 */
public class StandFormatAtomicSignID implements SignIDProvider {

    /**
     * signature ID自增提供者
     */
    private final AtomicInteger provider;

    public StandFormatAtomicSignID() {
        provider = new AtomicInteger(0);
    }

    /**
     * 创建指定最大signature ID signature ID提供器
     *
     * @param maxSignID 最大signature ID string
     */
    public StandFormatAtomicSignID(String maxSignID) {
        int maxSignIDNum = this.parse(maxSignID);
        provider = new AtomicInteger(maxSignIDNum);
    }


    /**
     * set current maximum signature ID value
     * <p>
     * implementor must parse this string and set the built-in counter
     *
     * @param maxSignId current maximum signature ID format string
     */
    @Override
    public void setCurrentMaxSignId(String maxSignId) {
        int maxSignIDNum = this.parse(maxSignId);
        provider.set(maxSignIDNum);
    }

    /**
     * increment and get signature ID
     *
     * @return signature ID，形如：'s001'
     */
    @Override
    public String incrementAndGet() {
        int newSignID = provider.incrementAndGet();
        return String.format("s%03d", newSignID);
    }

    /**
     * 获取当前signature ID
     * @return signature ID
     */
    @Override
    public String get(){
        int maxSignId = provider.get();
        return String.format("s%03d", maxSignId);
    }

    /**
     * parse the ID number of the electronic signature
     *
     * @param id ID string
     * @return ID number
     */
    @Override
    public int parse(String id) {
        return SignIdParser.parseIndex(id);
    }

}
