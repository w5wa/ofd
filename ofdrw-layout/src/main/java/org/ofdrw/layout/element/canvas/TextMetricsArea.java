package org.ofdrw.layout.element.canvas;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * 测量的文字区域信息
 *
 * @author Quan Guanyu
 * @since 2020-05-07 19:03:17
 */
public class TextMetricsArea {
    /**
     * 每个字符大小
     */
    public List<Rectangle2D> charAreas;

    /**
     * 文字区域width（单位毫米mm）
     * <p>
     * width = 每个字width + 字间距 * (n - 1) ，n 文字数量
     */
    public double width;

    /**
     * 文字区域height（单位毫米mm）
     */
    public double height;

    /**
     * character spacing
     */
    public double letterSpacing;

    public TextMetricsArea() {
    }
}
