package org.ofdrw.core.pageDescription.color.colorSpace;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 调色板
 * <p>
 * 8.3 颜色 表 25
 * <p>
 * color index numbers in the palette start from 0
 *
 * @author Quan Guanyu
 * @since 2019-10-11 08:30:07
 */
public class Palette extends OFDElement {
    public Palette(Element proxy) {
        super(proxy);
    }

    public Palette() {
        super("Palette");
    }

    /**
     * [required]
     * <p>
     * 设置 调色板中的预定义颜色
     *
     * @param cv 调色板中的预定义颜色
     * @return this
     */
    public Palette addCV(CV cv) {
        this.add(cv);
        return this;
    }

    /**
     * 获取 预定义位置颜色
     *
     * @param index 预定义位置颜色位置序号，0 开始
     * @return 预定义位置颜色
     */
    public CV getCVByIndex(Integer index) {
        if (index == null || index < 0) {
            throw new NumberFormatException("Index 必须大于等于0");
        }
        List<Element> elements = this.elements();
        if (index >= elements.size()) {
            throw new ArrayIndexOutOfBoundsException("预定义位置颜色不存在 Index：" + index);
        }
        return new CV(elements.get(index));
    }

    /**
     * 获取 预定义位置颜色
     *
     * @param index 预定义位置颜色位置序号，0 开始
     * @return 预定义位置颜色
     */
    public ST_Array getColorByIndex(Integer index) {
        return getCVByIndex(index).getColor();
    }

    /**
     * [required]
     * <p>
     * 获取 调色板中的预定义颜色
     * <p>
     * color index numbers in the palette start from 0
     *
     * <p>
     * tip：只读
     *
     * @return 调色板中的预定义颜色列表
     */
    public List<CV> getCVs() {
        return this.getOFDElements("CV",CV::new);
    }

}
