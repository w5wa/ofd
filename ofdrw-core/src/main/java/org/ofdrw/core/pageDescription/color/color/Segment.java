package org.ofdrw.core.pageDescription.color.color;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.STBase;

/**
 * 颜色segment
 * <p>
 * 至少出现两个
 * <p>
 * 8.3.4.2 Axial Gradient - Figure 29, 30, Table 29
 *
 * @author Quan Guanyu
 * @since 2019-10-31 07:57:48
 */
public class Segment extends OFDElement {
    public Segment(Element proxy) {
        super(proxy);
    }

    public Segment() {
        super("Segment");
    }

    /**
     * 构造segment颜色
     *
     * @param color segment颜色
     */
    public Segment(CT_Color color) {
        this();
        this.setColor(color);
    }

    /**
     * 构造segment颜色
     *
     * @param position segment坐标
     * @param color    segment颜色
     */
    public Segment(Double position, CT_Color color) {
        this();
        this.setColor(color)
                .setPosition(position);
    }


    /**
     * [optional attribute]
     * 设置 渐变segment颜色位置参数
     * <p>
     * 用于确定 StartPoint 和 EndPoint 中的各颜色的位置值，
     * 取值范围是 [0, 1.0]，各颜色的 Position 值应根据颜色出现
     * 的顺序递增第一个 Segment 的 Position 属性默认值为 0，最后
     * 一个 Segment 的 Position 属性default value: 1.0，当不存在时，
     * 在空缺的区间内平局分配。
     * <p>
     * 举例： Segment 个数等于 2 且不出现 Position 属性时，
     * 按照“0 1.0”处理；Segment 个数等于 3 且不出现 Position 属性时，
     * 按照“0 0.5 1.0”处理；Segment 个数等于 5 且不出现 Position 属性时，
     * 按照“0 0.25 0.5 0.75 1.0” 处理。
     *
     * @param position 渐变位置参数
     * @return this
     */
    public Segment setPosition(Double position) {
        if (position == null) {
            this.removeAttr("Position");
            return this;
        }
        this.addAttribute("Position", STBase.fmt(position));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 渐变segment颜色位置参数
     * <p>
     * 用于确定 StartPoint 和 EndPoint 中的各颜色的位置值，
     * 取值范围是 [0, 1.0]，各颜色的 Position 值应根据颜色出现
     * 的顺序递增第一个 Segment 的 Position 属性默认值为 0，最后
     * 一个 Segment 的 Position 属性default value: 1.0，当不存在时，
     * 在空缺的区间内平局分配。
     * <p>
     * 举例： Segment 个数等于 2 且不出现 Position 属性时，
     * 按照“0 1.0”处理；Segment 个数等于 3 且不出现 Position 属性时，
     * 按照“0 0.5 1.0”处理；Segment 个数等于 5 且不出现 Position 属性时，
     * 按照“0 0.25 0.5 0.75 1.0” 处理。
     *
     * @return 渐变位置参数
     */
    public Double getPosition() {
        String str = this.attributeValue("Position");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return Double.parseDouble(str);
    }


    /**
     * [required]
     * 设置 该segment的颜色
     * <p>
     * 应是基本颜色
     *
     * @param color 该segment的颜色，应是基本颜色
     * @return this
     */
    public Segment setColor(CT_Color color) {
        if (color == null) {
            throw new IllegalArgumentException("segment颜色（Color）为空");
        }
        this.removeAll();
        this.add(color);
        return this;
    }

    /**
     * [required]
     * 获取 该segment的颜色
     * <p>
     * 应是基本颜色
     *
     * @return 该segment的颜色，应是基本颜色
     */
    public CT_Color getColor() {
        Element e = this.getOFDElement("Color");
        return e == null ? null : new CT_Color(e);
    }

}
