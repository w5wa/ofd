package org.ofdrw.core.pageDescription.color.colorSpace;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Array;

/**
 * 调色板中预定义的颜色
 * <p>
 * color index numbers in the palette start from 0
 * <p>
 * 8.3 颜色 表 25
 */
public class CV extends OFDElement {
    public CV(Element proxy) {
        super(proxy);
    }

    public CV() {
        super("CV");
    }

    /**
     * color representation:
     * <p>
     * Gray - one channel for grayscale value; e.g. "#FF 255"
     * <p>
     * RGB - 3 channels: red, green, blue; e.g. "#11 #22 #33", "17 34 51"
     * <p>
     * CMYK - 4 channels: cyan, yellow, magenta, black; e.g. "#11 #22 #33 #44", "17 34 51 68"
     *
     * @param color 设置预定义的颜色
     */
    public CV(ST_Array color) {
        this();
        this.setColor(color);
    }

    /**
     * 设置 预定义的颜色
     * <p>
     * color representation:
     * <p>
     * Gray - one channel for grayscale value; e.g. "#FF 255"
     * <p>
     * RGB - 3 channels: red, green, blue; e.g. "#11 #22 #33", "17 34 51"
     * <p>
     * CMYK - 4 channels: cyan, yellow, magenta, black; e.g. "#11 #22 #33 #44", "17 34 51 68"
     *
     * @param value 设置预定义的颜色
     * @return this
     */
    public CV setColor(ST_Array value) {
        this.addText(value.toString());
        return this;
    }

    /**
     * 获取 预定义的颜色
     * <p>
     * color representation:
     * <p>
     * Gray - one channel for grayscale value; e.g. "#FF 255"
     * <p>
     * RGB - 3 channels: red, green, blue; e.g. "#11 #22 #33", "17 34 51"
     * <p>
     * CMYK - 4 channels: cyan, yellow, magenta, black; e.g. "#11 #22 #33 #44", "17 34 51 68"
     *
     * @return 设置预定义的颜色
     */
    public ST_Array getColor() {
        return ST_Array.getInstance(this.getText());
    }

}
