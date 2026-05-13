package org.ofdrw.layout.element.canvas;

import org.ofdrw.core.basicType.ST_Pos;
import org.ofdrw.core.pageDescription.color.color.CT_AxialShd;
import org.ofdrw.core.pageDescription.color.color.CT_Color;
import org.ofdrw.core.pageDescription.color.color.Segment;

/**
 * Canvas 渐变
 * 该类用于与HTML Canvas API兼容
 *
 * @author Quan Guanyu
 * @since 2023-4-11 22:53:00
 */
public class CanvasGradient {
    public final CT_AxialShd axialShd;

    /**
     * 创建一个线性渐变
     *
     * @param x0 start point x-coordinate
     * @param y0 start point y-coordinate
     * @param x1 end point x-coordinate
     * @param y1 end point y-coordinate
     */
    public CanvasGradient(double x0, double y0, double x1, double y1) {
        axialShd = new CT_AxialShd();
        axialShd.setStartPoint(ST_Pos.getInstance(x0, y0));
        axialShd.setEndPoint(ST_Pos.getInstance(x1, y1));
    }

    /**
     * 添加渐变颜色segment
     *
     * @param offset 渐变颜色位置，取值范围[0,1] 用于确定StartPoint和EndPoint之间的位置
     * @param color  16进制color value 或 颜色名，如#FF0000
     */
    public void addColorStop(double offset, String color) {
        int[] rgb = NamedColor.rgb(color);
        if (rgb == null) {
            return;
        }

        CT_Color c = CT_Color.rgb(rgb[0], rgb[1], rgb[2]);
        if (rgb.length > 3) {
            // 颜色参数contains透明度
            c.setAlpha(rgb[3]);
        }
        axialShd.addSegment(new Segment(offset, c));
    }

}
