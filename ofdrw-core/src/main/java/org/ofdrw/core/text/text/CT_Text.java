package org.ofdrw.core.text.text;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.pageObj.layer.block.TextObject;
import org.ofdrw.core.basicType.STBase;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.graph.pathObj.FillColor;
import org.ofdrw.core.graph.pathObj.StrokeColor;
import org.ofdrw.core.pageDescription.CT_GraphicUnit;
import org.ofdrw.core.pageDescription.clips.ClipAble;
import org.ofdrw.core.pageDescription.color.color.CT_Color;
import org.ofdrw.core.text.CT_CGTransform;
import org.ofdrw.core.text.TextCode;

import java.util.List;

/**
 * text object
 * <p>
 * 11.2 text object 图 59 表 45
 *
 * @author Quan Guanyu
 * @since 2019-10-18 09:27:41
 */
public class CT_Text extends CT_GraphicUnit<CT_Text> implements ClipAble {
    public CT_Text(Element proxy) {
        super(proxy);
    }

    public CT_Text(String name) {
        super(name);
    }

    public CT_Text() {
        super("Text");
    }

    /**
     * get text object
     *
     * @param id 文字object ID
     * @return text object TextObject
     */
    public static CT_Text textObject(ST_ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID 不能为空");
        }
        CT_Text res = new CT_Text("TextObject");
        res.setObjID(id);
        return res;
    }

    /**
     * 构造text object
     *
     * @param id object ID
     * @return object
     */
    public TextObject toObj(ST_ID id) {
        this.setOFDName("TextObject");
        this.setObjID(id);
        return new TextObject(this);
    }

    /**
     * [required attribute]
     * set glyph identifier referenced in the resource file
     *
     * @param font 引用glyph资源file path
     * @return this
     */
    public CT_Text setFont(ST_RefID font) {
        if (font == null) {
            throw new IllegalArgumentException("glyphresource file（Font）不能为空");
        }
        this.addAttribute("Font", font.toString());
        return this;
    }

    /**
     * [required attribute]
     * set glyph identifier referenced in the resource file
     *
     * @param refId ID
     * @return this
     */
    public CT_Text setFont(long refId) {
        return setFont(new ST_RefID(refId));
    }

    /**
     * [required attribute]
     * 获取 引用资源file path
     *
     * @return 引用glyph资源file path
     */
    public ST_RefID getFont() {
        return ST_RefID.getInstance(this.attributeValue("Font"));
    }

    /**
     * [required attribute]
     * 设置 字号，单位为毫米
     *
     * @param size 字号，单位为毫米
     * @return this
     */
    public CT_Text setSize(Double size) {
        if (size == null) {
            throw new IllegalArgumentException("字号（Size）不能为空");
        }
        this.addAttribute("Size", STBase.fmt(size));
        return this;
    }

    /**
     * [required attribute]
     * 获取 字号，单位为毫米
     *
     * @return 字号，单位为毫米
     */
    public Double getSize() {
        String str = this.attributeValue("Size");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("字号（Size）不能为空");
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 设置 是否勾边
     * <p>
     * default value: false
     *
     * @param stroke true - 勾边；false - 不勾边
     * @return this
     */
    public CT_Text setStroke(Boolean stroke) {
        if (stroke == null) {
            this.removeAttr("Stroke");
            return this;
        }
        this.addAttribute("Stroke", stroke.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 是否勾边
     * <p>
     * default value: false
     *
     * @return true - 勾边；false - 不勾边
     */
    public Boolean getStroke() {
        String str = this.attributeValue("Stroke");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional attribute]
     * 设置 是否填充
     * <p>
     * default value: true
     *
     * @param fill true - 填充；false - 不填充
     * @return this
     */
    public CT_Text setFill(Boolean fill) {
        if (fill == null) {
            this.removeAttr("Fill");
            return this;
        }
        this.addAttribute("Fill", fill.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 是否填充
     * <p>
     * default value: true
     *
     * @return true - 填充；false - 不填充
     */
    public Boolean getFill() {
        String str = this.attributeValue("Fill");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional attribute]
     * 设置 glyph在水平方向的缩放比
     * <p>
     * default value: 1.0
     * <p>
     * e.g., when HScale is 0.5, the actual display width is half the original character width.
     *
     * @param hScale glyph在水平方向的缩放比
     * @return this
     */
    public CT_Text setHScale(Double hScale) {
        if (hScale == null) {
            this.removeAttr("HScale");
            return this;
        }
        this.addAttribute("HScale", STBase.fmt(hScale));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 glyph在水平方向的缩放比
     * <p>
     * default value: 1.0
     * <p>
     * e.g., when HScale is 0.5, the actual display width is half the original character width.
     *
     * @return glyph在水平方向的缩放比
     */
    public Double getHScale() {
        String str = this.attributeValue("HScale");
        if (str == null || str.trim().length() == 0) {
            return 1.0d;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 指定 阅读方向
     * <p>
     * specifies text arrangement direction; see section 11.3 Text Positioning
     * <p>
     * default value: 0
     *
     * @param readDirection 阅读方向，可选值为{@link Direction}
     * @return this
     */
    public CT_Text setReadDirection(Direction readDirection) {
        if (readDirection == null) {
            this.removeAttr("ReadDirection");
            return this;
        }
        this.addAttribute("ReadDirection", readDirection.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 阅读方向
     * <p>
     * specifies text arrangement direction; see section 11.3 Text Positioning
     * <p>
     * default value: 0
     *
     * @return 阅读方向，可选值为{@link Direction}
     */
    public Direction getReadDirection() {
        return Direction.getInstance(this.attributeValue("ReadDirection"));
    }

    /**
     * [optional attribute]
     * 指定 字符方向
     * <p>
     * specifies text placement direction; see section 11.3 Text Positioning
     * <p>
     * default value: 0
     *
     * @param charDirection 字符方向，可选值为{@link Direction}
     * @return this
     */
    public CT_Text setCharDirection(Direction charDirection) {
        if (charDirection == null) {
            this.removeAttr("CharDirection");
            return this;
        }
        this.addAttribute("CharDirection", charDirection.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 字符方向
     * <p>
     * specifies text placement direction; see section 11.3 Text Positioning
     * <p>
     * default value: 0
     *
     * @return 字符方向，可选值为{@link Direction}
     */
    public Direction getCharDirection() {
        return Direction.getInstance(this.attributeValue("CharDirection"));
    }

    /**
     * [optional attribute]
     * set font weight of text object
     * <p>
     * default value: 400
     *
     * @param weight text object的粗细值，可选值{@link Weight}
     * @return this
     */
    public CT_Text setWeight(Weight weight) {
        if (weight == null) {
            this.removeAttr("Weight");
            return this;
        }
        this.addAttribute("Weight", weight.toString());
        return this;
    }

    /**
     * [optional attribute]
     * set font weight of text object
     * <p>
     * default value: 400
     *
     * @return text object的粗细值，可选值{@link Weight}
     */
    public Weight getWeight() {
        return Weight.getInstance(this.attributeValue("Weight"));
    }

    /**
     * [optional attribute]
     * 设置 是否是斜体样式
     * <p>
     * default value: false
     *
     * @param italic true - 斜体样式； false - 正常
     * @return this
     */
    public CT_Text setItalic(Boolean italic) {
        if (italic == null) {
            this.removeAttr("Italic");
            return this;
        }
        this.addAttribute("Italic", italic.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 是否是斜体样式
     * <p>
     * default value: false
     *
     * @return true - 斜体样式； false - 正常
     */
    public Boolean getItalic() {
        String str = this.attributeValue("Italic");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }


    /**
     * [optional]
     * set fill color
     * <p>
     * default: black
     *
     * @param fillColor fill color
     * @return this
     */
    public CT_Text setFillColor(CT_Color fillColor) {
        if (fillColor == null) {
            return this;
        }
        fillColor.setOFDName("FillColor");
        this.add(fillColor);
        return this;
    }

    /**
     * [optional]
     * get fill color
     * <p>
     * default: black
     *
     * @return fill color，null表示黑色
     */
    public FillColor getFillColor() {
        Element e = this.getOFDElement("FillColor");
        return e == null ? null : new FillColor(e);
    }


    /**
     * [optional]
     * set outline color
     * <p>
     * default: transparent
     *
     * @param strokeColor outline color
     * @return this
     */
    public CT_Text setStrokeColor(CT_Color strokeColor) {
        if (strokeColor == null) {
            return this;
        }
        strokeColor.setOFDName("StrokeColor");
        this.add(strokeColor);
        return this;
    }

    /**
     * [optional]
     * get outline color
     * <p>
     * default: transparent
     *
     * @return outline color，null为透明色
     */
    public StrokeColor getStrokeColor() {
        Element e = this.getOFDElement("StrokeColor");
        return e == null ? null : new StrokeColor(e);
    }

    /**
     * [optional]
     * 增加  指定字符编码到字符索引之间的变换关系
     * <p>
     * see section 11.4 Character Transformation
     *
     * @param cgTransform 字符编码到字符索引之间的变换关系
     * @return this
     */
    public CT_Text addCGTransform(CT_CGTransform cgTransform) {
        if (cgTransform == null) {
            return this;
        }
        this.add(cgTransform);
        return this;
    }

    /**
     * [optional]
     * 获取  指定字符编码到字符索引之间的变换关系序列
     * <p>
     * see section 11.4 Character Transformation
     *
     * @return 字符编码到字符索引之间的变换关系序列
     */
    public List<CT_CGTransform> getCGTransforms() {
        return this.getOFDElements("CGTransform", CT_CGTransform::new);
    }

    /**
     * [required]
     * 增加 文字内容
     * <p>
     * i.e., a segment character encoding string
     * <p>
     * 如果字符编码不在XML编码方式的字符范围之内，应采用“\”加四位
     * hex digits for escaping; spaces in text content also need to be escaped
     * when TextCode is used as a placeholder, always use ¤ (\u00A4)
     *
     * @param textCode 文字内容
     * @return this
     */
    public CT_Text addTextCode(TextCode textCode) {
        if (textCode == null) {
            return this;
        }
        this.add(textCode);
        return this;
    }

    /**
     * [required]
     * 获取 文字内容序列
     * <p>
     * i.e., a segment character encoding string
     * <p>
     * 如果字符编码不在XML编码方式的字符范围之内，应采用“\”加四位
     * hex digits for escaping; spaces in text content also need to be escaped
     * when TextCode is used as a placeholder, always use ¤ (\u00A4)
     *
     * @return 文字内容序列
     */
    public List<TextCode> getTextCodes() {
        return this.getOFDElements("TextCode", TextCode::new);
    }

}
