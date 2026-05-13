package org.ofdrw.core.basicStructure.res.resources;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.res.OFDResource;
import org.ofdrw.core.text.font.CT_Font;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * contains了文档所有glyph的描述
 * <p>
 * 7.9 Figure 20 Table 18
 *
 * @author Quan Guanyu
 * @since 2019-11-13 19:41:17
 */
public class Fonts extends OFDElement implements OFDResource {
    public Fonts(Element proxy) {
        super(proxy);
    }

    public Fonts() {
        super("Fonts");
    }


    /**
     * [required]
     * 添加 glyph描述
     * <p>
     * must have ID attribute
     *
     * @param font glyph描述
     * @return this
     */
    public Fonts addFont(CT_Font font) {
        if (font == null) {
            return this;
        }
        if (font.getID() == null) {
            throw new IllegalArgumentException("glyph描述ID不能为空");
        }
        this.add(font);
        return this;
    }

    /**
     * [required]
     * 获取 glyph描述序列
     * <p>
     * must have ID attribute
     *
     * @return glyph描述
     */
    public List<CT_Font> getFonts() {
        return this.getOFDElements("Font", CT_Font::new);
    }
}
