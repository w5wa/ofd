package org.ofdrw.graphics2d;

import org.ofdrw.core.graph.pathObj.AbbreviatedData;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.awt.geom.RoundRectangle2D;

/**
 * AWT路径转换工具
 * <p>
 * 将AWT的 {@link Shape} 转换为OFD的路径绘制数据 {@link AbbreviatedData}
 *
 * @author Quan Guanyu
 * @since 2023-01-18 16:13:58
 */
public class OFDShapes {


    /**
     * 转换 Java的图形对象为OFD路径对象
     *
     * @param shape Java图形对象
     * @return OFD 图形轮廓数据
     */
    public static AbbreviatedData path(final Shape shape) {
        final AbbreviatedData p = new AbbreviatedData();
        final double[] coords = new double[6];

        for (final PathIterator iterator = shape.getPathIterator(null); !iterator.isDone(); iterator.next()) {
            final int segType = iterator.currentSegment(coords);
            switch (segType) {
                case PathIterator.SEG_MOVETO:
                    p.moveTo((float) coords[0], (float) coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    p.lineTo((float) coords[0], (float) coords[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    p.quadraticBezier((float) coords[0], (float) coords[1],
                            (float) coords[2], (float) coords[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    p.cubicBezier((float) coords[0], (float) coords[1],
                            (float) coords[2], (float) coords[3],
                            (float) coords[4], (float) coords[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    p.close();
                    break;
                default:
                    System.err.println("未知的路径类型: " + segType);
            }
        }
        return p;
    }

    /**
     * 创建圆角矩形
     *
     * @param x         rectangle X coordinate of top-left corner
     * @param y         rectangle Y coordinate of top-left corner
     * @param width     rectangle width
     * @param height    rectangle height
     * @param arcWidth  horizontal corner radius
     * @param arcHeight vertical corner radius
     * @return 圆角矩形图形对象
     */
    public static RoundRectangle2D roundRect(int x, int y, int width, int height,
                                             int arcWidth, int arcHeight) {
        RoundRectangle2D roundRect = new RoundRectangle2D.Double(x, y, width, height,
                arcWidth, arcHeight);
        roundRect.setRoundRect(x, y, width, height,
                arcWidth, arcHeight);

        return roundRect;
    }

}
