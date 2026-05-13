package org.ofdrw.core.basicStructure.res;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.res.resources.*;

/**
 * resource file抽象类型
 * <p>
 * 用于代指：drawing parameters、color space、glyph、图像、音视频等资源的都为资源类型。
 *
 * @author Quan Guanyu
 * @since 2019-10-11 06:07:07
 */
public interface OFDResource extends Element {

    /**
     * 解析元素并获取对应资源类型子类instance
     *
     * @param element instance
     * @return 子类instance
     * @throws IllegalArgumentException 未知的element type不是 OFDResource子类
     */
    static OFDResource getInstance(Element element) {
        String qName = element.getQualifiedName();
        OFDResource res = null;
        switch (qName) {
            case "ofd:ColorSpaces":
            case "ColorSpaces":
                res = new ColorSpaces(element);
                break;
            case "ofd:DrawParams":
            case "DrawParams":
                res = new DrawParams(element);
                break;
            case "ofd:Fonts":
            case "Fonts":
                res = new Fonts(element);
                break;
            case "ofd:MultiMedias":
            case "MultiMedias":
                res = new MultiMedias(element);
                break;
            case "ofd:CompositeGraphicUnits":
            case "CompositeGraphicUnits":
                res = new CompositeGraphicUnits(element);
                break;
            default:
                if (qName.toLowerCase().contains("draw")) {
                    res = new DrawParams(element);
                } else if (qName.toLowerCase().contains("font")) {
                    res = new Fonts(element);
                } else if (qName.toLowerCase().contains("color")) {
                    res = new ColorSpaces(element);
                } else {
                    throw new IllegalArgumentException("不是 Res的子类，未知element type：" + qName);
                }
        }
        return res;
    }
}
