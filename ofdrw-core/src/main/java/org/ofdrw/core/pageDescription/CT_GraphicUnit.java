package org.ofdrw.core.pageDescription;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.action.Actions;
import org.ofdrw.core.basicType.STBase;
import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.pageDescription.clips.CT_Clip;
import org.ofdrw.core.pageDescription.clips.Clips;
import org.ofdrw.core.pageDescription.drawParam.LineCapType;
import org.ofdrw.core.pageDescription.drawParam.LineJoinType;

import java.util.List;

/**
 * 图元对象
 * <p>
 * 图元对象是版式文档中页面上呈现内容的最基本单元，
 * 所有页面显示内容。包括文字、图形、图像等，都属于
 * 图元对象，或是图元对象的组合。
 * <p>
 * 8.5 图元对象 图 45 表 34
 *
 * @author Quan Guanyu
 * @since 2019-10-14 07:32:38
 */
public abstract class CT_GraphicUnit<T extends CT_GraphicUnit> extends OFDElement {
    public CT_GraphicUnit(Element proxy) {
        super(proxy);
    }

    public CT_GraphicUnit(String name) {
        super(name);
    }

    /**
     * [required attribute]
     * 设置 外接矩形
     * <p>
     * using the current space coordinate system (page coordinates or other container coordinates); when a graphic
     * element drawing exceeds this rectangular area, clipping is applied.
     *
     * @param boundary 外接矩形
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setBoundary(ST_Box boundary) {
        if (boundary == null) {
            throw new IllegalArgumentException("外接矩形不能为空");
        }
        this.addAttribute("Boundary", boundary.toString());
        return (T) this;
    }

    /**
     * [required attribute]
     * 设置 外接矩形
     * <p>
     * using the current space coordinate system (page coordinates or other container coordinates); when a graphic
     * element drawing exceeds this rectangular area, clipping is applied.
     *
     * @param topLeftX 外接矩形X coordinate
     * @param topLeftY 外接矩形Y coordinate
     * @param width    外接rectangle width
     * @param height   外接rectangle height
     * @return this
     */
    public T setBoundary(double topLeftX, double topLeftY, double width, double height) {
        ST_Box boundary = new ST_Box(topLeftX, topLeftY, width, height);
        return setBoundary(boundary);
    }

    /**
     * [required attribute]
     * 获取 外接矩形
     * <p>
     * using the current space coordinate system (page coordinates or other container coordinates); when a graphic
     * element drawing exceeds this rectangular area, clipping is applied.
     *
     * @return 外接矩形
     */
    public ST_Box getBoundary() {
        return ST_Box.getInstance(this.attributeValue("Boundary"));
    }

    /**
     * [optional attribute]
     * 设置 图元对象的名字
     *
     * @param name 图元对象的名字
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setGraphicName(String name) {
        if (name == null || name.trim().length() == 0) {
            this.removeAttr("Name");
            return (T) this;
        }
        this.addAttribute("Name", name);
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取
     *
     * @return 图元对象的名字，可能为null
     */
    public String getGraphicName() {
        return this.attributeValue("Name");
    }

    /**
     * [optional attribute]
     * 设置 图元是否可见
     *
     * @param visible true - 可见；false - 不见
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setVisible(Boolean visible) {
        if (visible == null) {
            this.removeAttr("Visible");
            return (T) this;
        }
        this.addAttribute("Visible", visible.toString());
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 图元是否可见
     *
     * @return true - 可见；false - 不见
     */
    public Boolean getVisible() {
        String str = this.attributeValue("Visible");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }


    /**
     * [optional attribute]
     * 设置 对空间内的图元transformation matrix
     *
     * @param ctm transformation matrix
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setCTM(ST_Array ctm) {
        this.addAttribute("CTM", ctm.toString());
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 对空间内的图元transformation matrix
     *
     * @return transformation matrix
     */
    public ST_Array getCTM() {
        return ST_Array.getInstance(this.attributeValue("CTM"));
    }

    /**
     * [optional attribute]
     * 设置 引用resource file中的drawing parameters标识
     *
     * @param id drawing parameters标识
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setDrawParam(ST_RefID id) {
        this.addAttribute("DrawParam", id.toString());
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 引用resource file中的drawing parameters标识
     *
     * @return drawing parameters标识
     */
    public ST_RefID getDrawParam() {
        return ST_RefID.getInstance(this.attributeValue("DrawParam"));
    }


    /**
     * [optional attribute]
     * 设置 绘制路径时使用的线宽
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @param lineWidth 绘制路径时使用的线宽
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setLineWidth(Double lineWidth) {
        if (lineWidth == null) {
            this.removeAttr("LineWidth");
            return (T) this;
        }

        this.addAttribute("LineWidth", STBase.fmt(lineWidth));
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 绘制路径时使用的线宽
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @return 绘制路径时使用的线宽，可能为null
     */
    public Double getLineWidth() {
        String str = this.attributeValue("LineWidth");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 设置 线端点样式
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @param cap 线端点样式
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setCap(LineCapType cap) {
        if (cap == null) {
            this.removeAttr("Cap");
            return (T) this;
        }
        this.addAttribute("Cap", cap.toString());
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 线端点样式
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @return 线端点样式
     */
    public LineCapType getCap() {
        return LineCapType.getInstance(this.attributeValue("Cap"));
    }

    /**
     * [optional attribute]
     * 设置 线条连接样式
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @param join line join style
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setJoin(LineJoinType join) {
        if (join == null) {
            this.removeAttr("Join");
            return (T) this;
        }
        this.addAttribute("Join", join.toString());
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 线条连接样式
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @return line join style
     */
    public LineJoinType getJoin() {
        return LineJoinType.getInstance(this.attributeValue("Join"));
    }

    /**
     * [optional attribute]
     * 设置 Join的截断值
     * <p>
     * truncation value for small-angle join length when Join is Miter, default: 3.528.
     * this parameter is invalid when Join is not Miter.
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @param miterLimit Join的截断值长度
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setMiterLimit(Double miterLimit) {
        if (miterLimit == null) {
            this.removeAttr("MiterLimit");
            return (T) this;
        }
        this.addAttribute("MiterLimit", STBase.fmt(miterLimit));
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 Join的截断值
     * <p>
     * truncation value for small-angle join length when Join is Miter, default: 3.528.
     * this parameter is invalid when Join is not Miter.
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @return Join的截断值长度
     */
    public Double getMiterLimit() {
        String str = this.attributeValue("MiterLimit");
        if (str == null || str.trim().length() == 0) {
            return 3.528d;
        }
        return Double.parseDouble(str);
    }


    /**
     * [optional attribute]
     * 设置 线条虚线开始位置
     * <p>
     * default value: 0
     * <p>
     * this parameter is invalid when DashPattern is absent
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @param dashOffset 线条虚线开始位置
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setDashOffset(Double dashOffset) {
        if (dashOffset == null) {
            this.removeAttr("DashOffset");
            return (T) this;
        }
        this.addAttribute("DashOffset", STBase.fmt(dashOffset));
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 线条虚线开始位置
     * <p>
     * default value: 0
     * <p>
     * this parameter is invalid when DashPattern is absent
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @return 线条虚线开始位置
     */
    public Double getDashOffset() {
        String str = this.attributeValue("DashOffset");
        if (str == null || str.trim().length() == 0) {
            return 0d;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 设置 线条虚线的重复样式
     * <p>
     * the array contains two values; the first represents dash length,
     * the second represents gap length.
     * <p>
     * default value: null.
     * <p>
     * for line style control effects, see Table 23
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @param dashPattern 线条虚线的重复样式的数组中共含两个值，第一个值代表虚线的线segment的长度，第二个值代表虚线间隔的长度。
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setDashPattern(ST_Array dashPattern) {
        if (dashPattern == null) {
            this.removeAttr("DashPattern");
            return (T) this;
        }
        this.addAttribute("DashPattern", dashPattern.toString());
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 线条虚线的重复样式
     * <p>
     * the array contains two values; the first represents dash length,
     * the second represents gap length.
     * <p>
     * default value: null.
     * <p>
     * for line style control effects, see Table 23
     * <p>
     * see section 8.2 Drawing Parameters
     * <p>
     * if the graphic object has a DrawParam attribute, this value overrides the corresponding value in DrawParam
     *
     * @return 线条虚线的重复样式的数组中共含两个值，第一个值代表虚线的线segment的长度，第二个值代表虚线间隔的长度。
     */
    public ST_Array getDashPattern() {
        return ST_Array.getInstance(this.attributeValue("DashPattern"));
    }

    /**
     * [optional attribute]
     * 设置 图元对象透明度
     * <p>
     * 取值区间为 [0,255]
     * <p>
     * 默认为 255
     *
     * @param alpha 图元对象透明度，取值区间为 [0,255]
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setAlpha(Integer alpha) {
        if (alpha == null) {
            this.removeAttr("Alpha");
            return (T) this;
        }
        if (alpha < 0) {
            alpha = 255;
        }
        if (alpha > 255) {
            alpha = 255;
        }
        this.addAttribute("Alpha", alpha.toString());
        return (T) this;
    }

    /**
     * [optional attribute]
     * 获取 图元对象透明度
     * <p>
     * 取值区间为 [0,255]
     * <p>
     * 默认为 255
     *
     * @return 图元对象透明度，取值区间为 [0,255]
     */
    public Integer getAlpha() {
        String str = this.attributeValue("Alpha");
        if (str == null || str.trim().length() == 0) {
            return 255;
        }
        return Integer.parseInt(str);
    }


    /**
     * [optional]
     * 设置 图元对象的action sequence
     * <p>
     * when multiple Action objects exist, all actions are executed in order
     *
     * @param actions 图元对象的action sequence
     * @return this
     */
    @SuppressWarnings("unchecked")
    public T setActions(Actions actions) {
        this.add(actions);
        return (T) this;
    }

    /**
     * [optional]
     * 设置 图元对象的action sequence
     * <p>
     * when multiple Action objects exist, all actions are executed in order
     *
     * @return 图元对象的action sequence
     */
    public Actions getActions() {
        Element e = this.getOFDElement("Actions");
        return e == null ? null : new Actions(e);
    }


    /**
     * [optional]
     * 设置 图元对象的裁剪区域序列
     * <p>
     * uses the object space coordinate system
     * <p>
     * 当存在多个 Clip 对象时，最终裁剪区域为所有 Clip 区域的交集。
     *
     * @param clips 图元对象的裁剪区域序列
     * @return this
     */
    public CT_GraphicUnit<T> setClips(Clips clips) {
        if (clips == null) {
            return this;
        }

        List<CT_Clip> ctClips = clips.getClips();
        if (ctClips == null || ctClips.isEmpty()) {
            return this;
        }
        this.add(clips);
        return this;
    }

    /**
     * [optional]
     * 设置 图元对象的裁剪区域序列
     * <p>
     * uses the object space coordinate system
     * <p>
     * 当存在多个 Clip 对象时，最终裁剪区域为所有 Clip 区域的交集。
     *
     * @return 图元对象的裁剪区域序列
     */
    public Clips getClips() {
        Element e = this.getOFDElement("Clips");
        return e == null ? null : new Clips(e);
    }
}
