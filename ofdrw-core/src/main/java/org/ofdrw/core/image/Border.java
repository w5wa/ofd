package org.ofdrw.core.image;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.STBase;
import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.pageDescription.color.color.CT_Color;

/**
 * 图像边框
 * <p>
 * 10 表 43
 *
 * @author Quan Guanyu
 * @since 2019-10-27 02:45:46
 */
public class Border extends OFDElement {
    public Border(Element proxy) {
        super(proxy);
    }

    public Border() {
        super("Border");
    }

    /**
     * [optional attribute]
     * 设置 边框线宽
     * <p>
     * 如果为 0 则表示边框不进行绘制
     * <p>
     * default value: 0.353 mm
     *
     * @param lineWidth 边框线宽
     * @return this
     */
    public Border setLineWidth(Double lineWidth) {
        if (lineWidth == null) {
            this.removeAttr("LineWidth");
            return this;
        }
        this.addAttribute("LineWidth", STBase.fmt(lineWidth));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 边框线宽
     * <p>
     * 如果为 0 则表示边框不进行绘制
     * <p>
     * default value: 0.353 mm
     *
     * @return 边框线宽
     */
    public Double getLineWidth() {
        String str = this.attributeValue("LineWidth");
        if (str == null || str.trim().length() == 0) {
            return 0.353d;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 设置 边框水平角半径
     * <p>
     * default value: 0
     *
     * @param horizonalCornerRadius 边框水平角半径
     * @return this
     */
    public Border setHorizonalCornerRadius(Double horizonalCornerRadius) {
        if (horizonalCornerRadius == null) {
            this.removeAttr("HorizonalCornerRadius");
            return this;
        }
        this.addAttribute("HorizonalCornerRadius", STBase.fmt(horizonalCornerRadius));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 边框水平角半径
     * <p>
     * default value: 0
     *
     * @return 边框水平角半径
     */
    public Double getHorizonalCornerRadius() {
        String str = this.attributeValue("HorizonalCornerRadius");
        if (str == null || str.trim().length() == 0) {
            return 0d;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 设置 边框垂直角半径
     * <p>
     * default value: 0
     *
     * @param verticalCornerRadius 边框垂直角半径
     * @return this
     */
    public Border setVerticalCornerRadius(Double verticalCornerRadius) {
        if (verticalCornerRadius == null) {
            this.removeAttr("VerticalCornerRadius");
            return this;
        }
        this.addAttribute("VerticalCornerRadius", STBase.fmt(verticalCornerRadius));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 边框垂直角半径
     * <p>
     * default value: 0
     *
     * @return 边框垂直角半径
     */
    public Double getVerticalCornerRadius() {
        String str = this.attributeValue("VerticalCornerRadius");
        if (str == null || str.trim().length() == 0) {
            return 0d;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 设置 边框虚线重复样式开始的位置
     * <p>
     * border starts from the top-left corner, traversed clockwise
     * <p>
     * default value: 0
     *
     * @param dashOffset 边框虚线重复样式开始的位置
     * @return this
     */
    public Border setDashOffset(Double dashOffset) {
        if (dashOffset == null) {
            this.removeAttr("DashOffset");
            return this;
        }
        this.addAttribute("DashOffset", STBase.fmt(dashOffset));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 边框虚线重复样式开始的位置
     * <p>
     * border starts from the top-left corner, traversed clockwise
     * <p>
     * default value: 0
     *
     * @return 边框虚线重复样式开始的位置
     */
    public Double getDashOffset() {
        String str = this.attributeValue("DashOffset");
        if (str == null || str.trim().length() == 0) {
            return 0d;
        }
        return Double.parseDouble(str);
    }


    /**
     * [attribute, optional]
     * 设置 边框虚线重复样式
     * <p>
     * border starts from the top-left corner, traversed clockwise
     *
     * @param dashPattern 边框虚线重复样式
     * @return this
     */
    public Border setDashPattern(ST_Array dashPattern) {
        if (dashPattern == null) {
            this.removeAttr("DashPattern");
            return this;
        }
        this.addAttribute("DashPattern", dashPattern.toString());
        return this;
    }

    /**
     * [attribute, optional]
     * 获取 边框虚线重复样式
     * <p>
     * border starts from the top-left corner, traversed clockwise
     *
     * @return 边框虚线重复样式
     */
    public ST_Array getDashPattern() {
        return ST_Array.getInstance(this.attributeValue("DashPattern"));
    }


    /**
     * [optional]
     * set border color
     * <p>
     * see 8.3.2 Basic Colors for border color description
     * <p>
     * default: black
     *
     * @param borderColor border color
     * @return this
     */
    public Border setBorderColor(CT_Color borderColor) {
        if (borderColor == null) {
            return this;
        }
        if (!(borderColor instanceof BorderColor)) {
            this.setOFDName("BorderColor");
        }
        this.add(borderColor);
        return this;
    }

    /**
     * [optional]
     * 获取 border color
     * <p>
     * see 8.3.2 Basic Colors for border color description
     * <p>
     * default: black
     *
     * @return border color，null表示为黑色
     */
    public BorderColor getBorderColor() {
        Element e = this.getOFDElement("BorderColor");
        return e == null ? null : new BorderColor(e);
    }
}
