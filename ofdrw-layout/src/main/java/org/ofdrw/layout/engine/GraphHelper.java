package org.ofdrw.layout.engine;

import org.ofdrw.core.graph.pathObj.AbbreviatedData;

/**
 * 图形工具
 * <p>
 * 用于快速构建以路径为基础的图形图像数据
 *
 * @author Quan Guanyu
 * @since 2020-03-21 13:50:20
 */
public class GraphHelper {

    /**
     * 创建一个矩形轮廓
     *
     * @param x rectangle X coordinate of top-left corner
     * @param y rectangle Y coordinate of top-left corner
     * @param w rectangle width
     * @param h rectangle height
     * @return 矩形路径轮廓
     */
    public static AbbreviatedData rect(double x, double y, double w, double h) {
        return new AbbreviatedData()
                .M(x, y)
                .L(x + w, y)
                .L(x + w, y + h)
                .L(x, y + h)
                .C();
    }
}
