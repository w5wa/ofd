package org.ofdrw.sign;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * number格式的自增的signature ID
 * <p>
 * 启用前缀格式为： "NNN"，例如：'001'
 * <p>
 * 关闭前缀格式为： "N"，例如：'1' （默认）
 *
 * @author Quan Guanyu
 * @since 2020-04-17 04:20:15
 */
public class NumberFormatAtomicSignID implements SignIDProvider {
    /**
     * signature ID自增提供者
     */
    private final AtomicInteger provider;

    /**
     * 是否增加0前缀
     * <p>
     * 默认关闭0前缀
     */
    private boolean enableZeroPrefix = false;

    public NumberFormatAtomicSignID() {
        provider = new AtomicInteger(0);
    }

    /**
     * 创建number构造器
     *
     * @param enableZeroPrefix 是否启用前缀0，启用后ID格式为'00N'
     */
    public NumberFormatAtomicSignID(boolean enableZeroPrefix) {
        provider = new AtomicInteger(0);
        this.enableZeroPrefix = enableZeroPrefix;
    }

    /**
     * 创建指定最大signature ID signature ID提供器
     *
     * @param maxSignID 最大signature ID string
     */
    public NumberFormatAtomicSignID(String maxSignID) {
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
     * @return signature ID，形如：'001'
     */
    @Override
    public String incrementAndGet() {
        int newSignID = provider.incrementAndGet();
        if (enableZeroPrefix) {
            return String.format("%03d", newSignID);
        } else {
            return String.valueOf(newSignID);
        }
    }

    /**
     * 获取当前signature ID
     *
     * @return signature ID
     */
    @Override
    public String get() {
        int maxSignId = provider.get();
        if (enableZeroPrefix) {
            return String.format("%03d", maxSignId);
        } else {
            return String.valueOf(maxSignId);
        }
    }

    /**
     * parse the ID number of the electronic signature
     *
     * @param id ID string
     * @return ID number
     */
    @Override
    public int parse(String id) {
        return Integer.parseInt(id);
    }


    /**
     * 开关是否启用 0前缀
     *
     * @param enableZeroPrefix true - 启用0前缀； false - 关闭
     */
    public void setEnableZeroPrefix(boolean enableZeroPrefix) {
        this.enableZeroPrefix = enableZeroPrefix;
    }

    /**
     * 是否启用0前缀
     *
     * @return true - 启用0前缀
     */
    public boolean isEnableZeroPrefix() {
        return enableZeroPrefix;
    }
}
