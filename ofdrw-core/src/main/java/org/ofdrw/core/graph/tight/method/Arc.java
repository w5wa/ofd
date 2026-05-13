package org.ofdrw.core.graph.tight.method;

import org.dom4j.Element;
import org.ofdrw.core.basicType.STBase;
import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.basicType.ST_Pos;

/**
 * 圆弧
 * <p>
 * 图 56圆弧的结构
 *
 * @author Quan Guanyu
 * @since 2019-10-05 05:38:53
 */
public class Arc extends Command {
    public Arc(Element proxy) {
        super(proxy);
    }

    public Arc() {
        super("Arc");
    }

    public Arc(double rx, double ry,
               double angle,
               double large,
               double sweep,
               double x, double y) {
        this();
        setEllipseSize(rx, ry);
        setRotationAngle(angle);
        setLargeArc(large != 0);
        setSweepDirection(sweep != 0);
        setEndPoint(x, y);
    }

    /**
     * [required attribute]
     * 设置 弧线方向是否顺时针
     * <p>
     * true 表示由圆弧起始点到结束点是顺时针，false 表示由圆弧起始点到结束点是逆时针
     * <p>
     * 对于经过坐标系上指定两点，给定rotation angle和长短轴长度的椭圆，满足条件的可能有 2 个，
     * 对应的圆弧有 4 条，通过 LargeArc 属性可以排除 2 条，次属性从剩余的 2 条圆弧中确定
     * 一条
     *
     * @param sweepDirection true - 由圆弧起始点到结束点是顺时针;false - 由圆弧起始点到结束点是逆时针
     * @return this
     */
    public Arc setSweepDirection(boolean sweepDirection) {
        this.addAttribute("SweepDirection", Boolean.toString(sweepDirection));
        return this;
    }

    /**
     * [required attribute]
     * 获取 弧线方向是否顺时针
     * <p>
     * true 表示由圆弧起始点到结束点是顺时针，false 表示由圆弧起始点到结束点是逆时针
     * <p>
     * 对于经过坐标系上指定两点，给定rotation angle和长短轴长度的椭圆，满足条件的可能有 2 个，
     * 对应的圆弧有 4 条，通过 LargeArc 属性可以排除 2 条，次属性从剩余的 2 条圆弧中确定
     * 一条
     *
     * @return true - 由圆弧起始点到结束点是顺时针;false - 由圆弧起始点到结束点是逆时针
     */
    public Boolean getSweepDirection() {
        return Boolean.parseBoolean(this.attributeValue("SweepDirection"));
    }

    /**
     * [required attribute]
     * 设置 是否是大圆弧
     * <p>
     * true 表示此线型对应的位角度大于 180°的弧，false 表示对应度数小于 180°的弧
     * <p>
     * 对于一个给定长、短轴的椭圆以及起始点和结束点，有一大一小两条圆弧，
     * 如果所描述线型恰好为 180°的弧，此attribute value不被参考，可由 SweepDirection 属性确定圆弧形状
     *
     * @param largeArc true - 此线型对应的位角度大于 180°的弧；false - 对应度数小于 180°的弧
     * @return this
     */
    public Arc setLargeArc(boolean largeArc) {
        this.addAttribute("LargeArc", Boolean.toString(largeArc));
        return this;
    }

    /**
     * [required attribute]
     * 获取 是否是大圆弧
     * <p>
     * true 表示此线型对应的位角度大于 180°的弧，false 表示对应度数小于 180°的弧
     * <p>
     * 对于一个给定长、短轴的椭圆以及起始点和结束点，有一大一小两条圆弧，
     * 如果所描述线型恰好为 180°的弧，此attribute value不被参考，可由 SweepDirection 属性确定圆弧形状
     *
     * @return true - 此线型对应的位角度大于 180°的弧；false - 对应度数小于 180°的弧
     */
    public Boolean getLargeArc() {
        return Boolean.parseBoolean(this.attributeValue("LargeArc"));
    }


    /**
     * [required attribute]
     * 设置 按 EllipseSize 绘制的椭圆在当前坐标系下旋转的角度，
     * 正值为顺时针，负值为逆时针
     * <p>
     * [异常处理] 如果角度大于 360°，则以 360°取模
     *
     * @param rotationAngle 绘制的椭圆在当前坐标系下旋转的角度，正值为顺时针，负值为逆时针
     * @return this
     */
    public Arc setRotationAngle(double rotationAngle) {
        rotationAngle %= 360.0d;
        this.addAttribute("RotationAngle", STBase.fmt(rotationAngle));
        return this;
    }

    /**
     * [required attribute]
     * 获取 按 EllipseSize 绘制的椭圆在当前坐标系下旋转的角度，
     * 正值为顺时针，负值为逆时针
     * <p>
     * [异常处理] 如果角度大于 360°，则以 360°取模
     *
     * @return 绘制的椭圆在当前坐标系下旋转的角度，正值为顺时针，负值为逆时针
     */
    public Double getRotationAngle() {
        double res = Double.parseDouble(this.attributeValue("RotationAngle"));
        return res % 360.0d;
    }

    /**
     * [required attribute]
     * 设置 长短轴
     * <p>
     * array like [200 100] with 2 float values for the major and minor axes of the ellipse; the larger is the major axis
     * <p>
     * [exception handling] if array length > 2, only the first two values are used
     * <p>
     * [exception handling] if array length is 1, it is treated as a circle with this value as the radius
     * <p>
     * [exception handling] if either of the first two values is 0, or the array is empty, the arc degenerates into a line from the current point
     * to EndPoint
     * <p>
     * [exception handling]
     *
     * @param ellipseSize 形如[200 100]的数组，2个浮点数值一次对应椭圆的长、短轴长度，较大的一个为长轴
     * @return this
     */
    public Arc setEllipseSize(ST_Array ellipseSize) {
        if (ellipseSize == null) {
            throw new IllegalArgumentException("ellipseSize 不能为null");
        }
        this.addAttribute("EllipseSize", ellipseSize.toString());
        return this;
    }

    /**
     * [required attribute]
     * 设置 长短轴
     * <p>
     * array like [200 100] with 2 float values for the major and minor axes of the ellipse; the larger is the major axis
     * <p>
     * [exception handling] if array length > 2, only the first two values are used
     * <p>
     * [exception handling] if array length is 1, it is treated as a circle with this value as the radius
     * <p>
     * [exception handling] if either of the first two values is 0, or the array is empty, the arc degenerates into a line from the current point
     * to EndPoint
     * <p>
     * [exception handling]
     *
     * @param sizes 长短轴参数
     * @return this
     */
    public Arc setEllipseSize(double... sizes) {
        return setEllipseSize(new ST_Array(sizes));
    }


    /**
     * [required attribute]
     * 获取 长短轴
     * <p>
     * array like [200 100] with 2 float values for the major and minor axes of the ellipse; the larger is the major axis
     * <p>
     * [exception handling] if array length > 2, only the first two values are used
     * <p>
     * [exception handling] if array length is 1, it is treated as a circle with this value as the radius
     * <p>
     * [exception handling] if either of the first two values is 0, or the array is empty, the arc degenerates into a line from the current point
     * to EndPoint
     * <p>
     * [exception handling]
     *
     * @return 形如[200 100]的数组，2个浮点数值一次对应椭圆的长、短轴长度，较大的一个为长轴
     */
    public ST_Array getEllipseSize() {
        return ST_Array.getInstance(this.attributeValue("EllipseSize"));
    }

    /**
     * [required attribute]
     * set arc end point, which is the next path starting point
     * <p>
     * cannot be at the same position as the current drawing point
     *
     * @param endPoint 圆弧结束点，下一个路径起点
     * @return this
     */
    public Arc setEndPoint(ST_Pos endPoint) {
        this.addAttribute("EndPoint", endPoint.toString());
        return this;
    }

    /**
     * [required attribute]
     * set arc end point, which is the next path starting point
     * <p>
     * cannot be at the same position as the current drawing point
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return this
     */
    public Arc setEndPoint(double x, double y) {
        return setEndPoint(new ST_Pos(x, y));
    }

    /**
     * [required attribute]
     * set arc end point, which is the next path starting point
     * <p>
     * cannot be at the same position as the current drawing point
     *
     * @return 圆弧结束点，下一个路径起点
     */
    public ST_Pos getEndPoint() {
        return ST_Pos.getInstance(this.attributeValue("EndPoint"));
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("A ");
        final ST_Array ellipseSize = this.getEllipseSize();
        if (ellipseSize != null) {
            sb.append(ellipseSize.toString()).append(" ");
        } else {
            sb.append("0 0 ");
        }
        try {
            sb.append(getRotationAngle()).append(" ");
        } catch (NumberFormatException e) {
            sb.append("0 ");
        }
        if (getLargeArc()) {
            sb.append("1 ");
        } else {
            sb.append("0 ");
        }
        if (getSweepDirection()) {
            sb.append("1 ");
        } else {
            sb.append("0 ");
        }
         ST_Pos endPoint = getEndPoint();
        if (endPoint != null) {
            sb.append(endPoint.toString());
        }else{
            sb.append("0 0");
        }
        return sb.toString();
    }
}
