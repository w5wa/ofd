package org.ofdrw.layout.element;

/**
 * 对segment的占用情况
 *
 * @author Quan Guanyu
 * @since 2020-02-03 01:01:38
 */
public enum Clear {
    /**
     * 共享: 两side都允许出现元素。
     */
    none,
    /**
     * 左side不允许出现元素
     */
    left,
    /**
     * 右side不允许出现元素
     */
    right,
    /**
     * 两side不允许出现元素
     * <p>
     * default value
     */
    both;
}
