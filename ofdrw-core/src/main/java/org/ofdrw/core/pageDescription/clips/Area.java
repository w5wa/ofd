package org.ofdrw.core.pageDescription.clips;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.basicType.ST_RefID;

import java.util.List;

/**
 * clipping area
 * <p>
 * 用一个图形或text object来描述裁剪区的一个组成部分，
 * the final clipping area is the union of these regions.
 * <p>
 * 8.4 裁剪区 表 33
 */
public class Area extends OFDElement {

    public Area(Element proxy) {
        super(proxy);
    }

    public Area() {
        super("Area");
    }

    /**
     * [optional attribute]
     * 设置 引用resource file中的drawing parameters的标识
     * <p>
     * 线宽、结合点和端点样式等绘制特性对裁剪效果会产生影响，
     * 有关drawing parameters的描述见 8.2
     *
     * @param drawParam 引用resource file中的drawing parameters的标识
     * @return this
     */
    public Area setDrawParam(ST_RefID drawParam) {
        this.addAttribute("DrawParam", drawParam.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 引用resource file中的drawing parameters的标识
     * <p>
     * 线宽、结合点和端点样式等绘制特性对裁剪效果会产生影响，
     * 有关drawing parameters的描述见 8.2
     *
     * @return 引用resource file中的drawing parameters的标识
     */
    public ST_RefID getDrawParam() {
        return ST_RefID.getInstance(this.attributeValue("DrawParam"));
    }

    /**
     * [optional attribute]
     * 设置 transformation matrix
     * <p>
     * 针对对象坐标系，对Area下contains的 Path 和 Text 进行进一步的变换
     *
     * @param ctm transformation matrix
     * @return this
     */
    public Area setCTM(ST_Array ctm) {
        this.addAttribute("CTM", ctm.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 transformation matrix
     * <p>
     * 针对对象坐标系，对Area下contains的 Path 和 Text 进行进一步的变换
     *
     * @return transformation matrix
     */
    public ST_Array getCTM() {
        return ST_Array.getInstance(this.attributeValue("CTM"));
    }


    /**
     * [required]
     * 设置 裁剪对象
     * <p>
     * 裁剪对象可以是 CT_Text、CT_Path
     *
     * @param clipObj 裁剪对象
     * @return this
     */
    public Area setClipObj(ClipAble clipObj) {
        this.add(clipObj);
        return this;
    }

    /**
     * [required]
     * 获取 裁剪对象
     * <p>
     * 裁剪对象可以是 CT_Text、CT_Path
     *
     * @return 裁剪对象
     */
    public ClipAble getClipObj() {
        List<Element> elements = this.elements();
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        Element e = elements.get(0);
        return e == null ? null : ClipAble.getInstance(e);
    }
}
