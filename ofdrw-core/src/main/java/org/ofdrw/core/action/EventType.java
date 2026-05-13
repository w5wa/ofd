package org.ofdrw.core.action;

/**
 * 事件类型
 * <p>
 * 参照 52 事件类型
 *
 * @author Quan Guanyu
 * @since 2019-10-05 11:38:04
 */
public enum EventType {
    /**
     * 文档打开
     */
    DO,
    /**
     * 页面打开
     */
    PO,
    /**
     * 单击区域
     */
    CLICK;

    /**
     * 根据string获取匹配类型instance
     *
     * @param event 事件名称，只能是 DO，PO，CLICK
     * @return instance
     * @throws IllegalArgumentException 未知类型事件
     */
    public static EventType getInstance(String event) {
        event = event == null ? "" : event.trim();
        if (event.equalsIgnoreCase("DO")){
            return DO;
        } else if (event.equalsIgnoreCase("PO")){
            return PO;
        } else if (event.equalsIgnoreCase("CLICK")){
            return CLICK;
        } else {
            throw new IllegalArgumentException("未知类型事件： " + event);
        }
    }
}
