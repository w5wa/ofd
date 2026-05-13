package org.ofdrw.core.basicStructure.res.resources;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.res.OFDResource;
import org.ofdrw.core.pageDescription.color.colorSpace.CT_ColorSpace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * contains了一组color space的描述
 * <p>
 * 7.9 Figure 20 Table 18
 *
 * @author Quan Guanyu
 * @since 2019-11-13 07:27:47
 */
public class ColorSpaces extends OFDElement implements OFDResource {
    public ColorSpaces(Element proxy) {
        super(proxy);
    }

    public ColorSpaces() {
        super("ColorSpaces");
    }

    /**
     * [required]
     * 增加  color space描述
     * <p>
     * must have ID attribute
     *
     * @param colorSpace color space描述，必须要有ID属性
     * @return this
     */
    public ColorSpaces addColorSpace(CT_ColorSpace colorSpace) {
        if (colorSpace == null) {
            return this;
        }
        if (colorSpace.getID() == null) {
            throw new IllegalArgumentException("color spaceID不能为空");
        }
        this.add(colorSpace);
        return this;
    }

    /**
     * [required]
     * 获取  color space描述列表
     *
     * @return color space描述列表
     */
    public List<CT_ColorSpace> getColorSpaces() {
        return this.getOFDElements("ColorSpace", CT_ColorSpace::new);
    }
}
