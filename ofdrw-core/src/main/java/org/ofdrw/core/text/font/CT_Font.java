package org.ofdrw.core.text.font;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * glyph
 * <p>
 * 11.1 glyph 图 58 表 44
 *
 * @author Quan Guanyu
 * @since 2019-10-18 08:26:16
 */
public class CT_Font extends OFDElement {
    public CT_Font(Element proxy) {
        super(proxy);
    }

    public CT_Font() {
        super("Font");
    }

    /**
     * @param fontName glyph name
     */
    public CT_Font(String fontName) {
        this();
        this.setFontName(fontName);
    }

    public ST_ID getID() {
        return this.getObjID();
    }

    public CT_Font setID(ST_ID id) {
        this.setObjID(id);
        return this;
    }

    public CT_Font setID(long id) {
        this.setObjID(new ST_ID(id));
        return this;
    }

    /**
     * [required attribute]
     * 设置 glyph name
     *
     * @param fontName glyph name
     * @return this
     */
    public CT_Font setFontName(String fontName) {
        if (fontName == null) {
            throw new IllegalArgumentException("glyph name（fontName）不能为空");
        }
        this.addAttribute("FontName", fontName.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 glyph name
     *
     * @return glyph name
     */
    public String getFontName() {
        String str = this.attributeValue("FontName");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("glyph name（fontName）不能为空");
        }
        return str;
    }

    /**
     * [optional attribute]
     * 设置 glyph族名
     * <p>
     * used for matching substitute glyphs
     *
     * @param familyName glyph族名
     * @return this
     */
    public CT_Font setFamilyName(String familyName) {
        if (familyName == null || familyName.trim().length() == 0) {
            // 不设置（可选参数）
            return this;
        }
        this.addAttribute("FamilyName", familyName);
        return this;
    }

    /**
     * [optional attribute]
     * 获取 glyph族名
     * <p>
     * used for matching substitute glyphs
     *
     * @return glyph族名
     */
    public String getFamilyName() {
        return this.attributeValue("FamilyName");
    }

    /**
     * [optional attribute]
     * 设置 glyph适用的字符分类
     * <p>
     * optional values see {@link Charset}
     *
     * @param charset glyph适用的字符分类
     * @return this
     */
    public CT_Font setCharset(Charset charset) {
        if (charset == null) {
            charset = Charset.unicode;
        }
        this.addAttribute("Charset", charset.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 glyph适用的字符分类
     * optional values see {@link Charset}
     *
     * @return glyph适用的字符分类
     */
    public Charset getCharset() {
        return Charset.getInstance(this.attributeValue("Charset"));
    }

    /**
     * [optional attribute]
     * 设置 是否是斜体
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @param italic true - 斜体； false - 正常
     * @return this
     */
    public CT_Font setItalic(Boolean italic) {
        if (italic == null) {
            italic = false;
        }
        this.addAttribute("Italic", italic.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 是否是斜体
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @return true - 斜体； false - 正常
     */
    public Boolean getItalic() {
        String str = this.attributeValue("Italic");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional attribute]
     * 设置 是否是粗font
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @param bold true - 粗体； false - 正常
     * @return this
     */
    public CT_Font setBold(Boolean bold) {
        if (bold == null) {
            bold = false;
        }
        this.addAttribute("Bold", bold.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 是否是粗font
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @return true - 粗体； false - 正常
     */
    public Boolean getBold() {
        String str = this.attributeValue("Bold");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional attribute]
     * 设置 是否是带衬线glyph
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @param serif true - 带衬线；false - 正常
     * @return this
     */
    public CT_Font setSerif(Boolean serif) {
        if (serif == null) {
            serif = false;
        }
        this.addAttribute("Serif", serif.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 是否是带衬线glyph
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @return true - 带衬线；false - 正常
     */
    public Boolean getSerif() {
        String str = this.attributeValue("Serif");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional attribute]
     * set whether it is a monospaced glyph
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @param fixedWidth true - 等宽glyph；false - 正常
     * @return this
     */
    public CT_Font setFixedWidth(Boolean fixedWidth) {
        if (fixedWidth == null) {
            fixedWidth = false;
        }
        this.addAttribute("FixedWidth", fixedWidth.toString());
        return this;
    }

    /**
     * [optional attribute]
     * set whether it is a monospaced glyph
     * <p>
     * used for matching substitute glyphs
     * <p>
     * default value: false
     *
     * @return true - 等宽glyph；false - 正常
     */
    public Boolean getFixedWidth() {
        String str = this.attributeValue("FixedWidth");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional]
     * 设置 points to内嵌glyph文件
     * <p>
     * embedded glyph files should use OpenType format
     *
     * @param fontFile points to内嵌glyphfile path
     * @return this
     */
    public CT_Font setFontFile(ST_Loc fontFile) {
        this.setOFDEntity("FontFile", fontFile.toString());
        return this;
    }

    public CT_Font setFontFile(String path) {
        return setFontFile(new ST_Loc(path));
    }

    /**
     * [optional]
     * 获取 points to内嵌glyph文件
     * <p>
     * embedded glyph files should use OpenType format
     *
     * @return points to内嵌glyphfile path
     */
    public ST_Loc getFontFile() {
        return ST_Loc.getInstance(this.getOFDElementText("FontFile"));
    }
}
