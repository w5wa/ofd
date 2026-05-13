package org.ofdrw.layout.element.canvas;

/**
 * 测量的文字信息
 *
 * @author Quan Guanyu
 * @since 2020-05-07 19:03:17
 */
public class TextMetrics {

    /**
     * 阅读方向
     */
    public int readDirection;

    /**
     * 文字width（单位毫米mm）
     * <p>
     * 如果 readDirection == 0 || 180 为width
     * <p>
     * 如果 readDirection == 90 || 270 为height
     */
    public Double width;

    /**
     * 文本font大小（单位毫米mm）
     */
    public Double fontSize;


    /**
     * 后一个字对前一个字的偏移量
     */
    public Double[] offset;
}
