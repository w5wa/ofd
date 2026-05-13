package org.ofdrw.layout.element.canvas;

import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.graph.pathObj.AbbreviatedData;
import org.ofdrw.core.pageDescription.drawParam.CT_DrawParam;
import org.ofdrw.font.FontName;

/**
 * 画布上下文中的drawing parameters状态
 *
 * @author Quan Guanyu
 * @since 2020-05-06 19:22:15
 */
public class CanvasState implements Cloneable {

    /**
     * 上下文 路径数据
     */
    AbbreviatedData path;

    /**
     * transformation matrix
     */
    ST_Array ctm = null;

    /**
     * 绘制text settings
     * <p>
     * 默认为宋体，字号为1
     */
    FontSetting font = null;


    /**
     * 透明值。必须介于 0.0（完全透明） 与 1.0（不透明） 之间。
     */
    Double globalAlpha = null;

    /**
     * drawing parameters
     */
    CT_DrawParam drawParam;

    /**
     * clipping area
     */
    AbbreviatedData clipArea = null;

    /**
     * fill color 16进制格式
     * 如： #000000
     */
    Object fillStyle;

    /**
     * stroke color 16进制格式
     * 如： #000000
     */
    Object strokeStyle;

    /**
     * font样式
     */
    String fontStyle;

    public CanvasState() {
        drawParam = new CT_DrawParam();
        font = new FontSetting(1d, FontName.SimSun.font());
    }

    /**
     * 获取drawing parameters缓存
     * <p>
     * 如果缓存不存在那么创建
     *
     * @return drawing parameters缓存
     * @deprecated 采用 {@link #getDrawParam()}
     */
    @Deprecated
    public DrawParamCache obtainDrawParamCache() {
        return  new DrawParamCache();
    }

    /**
     * get drawing parameters
     *
     * @return drawing parameters
     */
    public CT_DrawParam getDrawParam() {
        return  this.drawParam;
    }


    @Override
    public CanvasState clone() {
        CanvasState that = new CanvasState();
        if (path != null) {
            that.path = path.clone();
        }
        if (ctm != null) {
            that.ctm = ctm.clone();
        }
        if (font != null) {
            that.font = font.clone();
        }
        if (globalAlpha != null) {
            that.globalAlpha = globalAlpha;
        }
        if (this.drawParam != null){
            this.drawParam = this.drawParam.clone();
        }
        if (clipArea != null) {
            that.clipArea = clipArea.clone();
        }
        that.fillStyle = this.fillStyle;
        that.strokeStyle = this.strokeStyle;
        that.fontStyle = this.fontStyle;
        return that;
    }
}
