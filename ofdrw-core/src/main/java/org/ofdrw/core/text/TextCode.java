package org.ofdrw.core.text;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.STBase;
import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.pageDescription.clips.ClipAble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文字定位
 * <p>
 * text object使用严格的文字定位信息进行定位
 * <p>
 * 11.3 文字定位 图 61 表 46
 *
 * @author Quan Guanyu
 * @since 2019-10-21 09:28:35
 */
public class TextCode extends OFDElement implements ClipAble {
    public TextCode(Element proxy) {
        super(proxy);
    }

    public TextCode() {
        super("TextCode");
    }

    /**
     * 设置文字内容
     *
     * @param content 内容
     * @return this
     */
    public TextCode setContent(String content) {
        this.setText(content);
        return this;
    }

    /**
     * 获取文字内容
     *
     * @return 文字内容
     */
    public String getContent() {
        return this.getText();
    }

    /**
     * 设置坐标
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return this
     */
    public TextCode setCoordinate(Double x, Double y) {
        return this.setX(x)
                .setY(y);
    }

    /**
     * [optional attribute]
     * set X coordinate of first character glyph in object coordinate system
     * <p>
     * when X is absent, use the X value of the previous TextCode; one per text object
     * TextCode attribute is required
     *
     * @param x 第一个文字的glyph在对象坐标系下的 X 坐标
     * @return this
     */
    public TextCode setX(Double x) {
        if (x == null) {
            this.removeAttr("X");
            return this;
        }
        this.addAttribute("X", STBase.fmt(x));
        return this;
    }

    /**
     * [optional attribute]
     * set X coordinate of first character glyph in object coordinate system
     * <p>
     * when X is absent, use the X value of the previous TextCode; one per text object
     * TextCode attribute is required
     *
     * @return 第一个文字的glyph在对象坐标系下的 X 坐标；null表示采用上一个 TextCode 的 X 值
     */
    public Double getX() {
        String str = this.attributeValue("X");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * 设置 第一个文字的glyph原点在对象坐标系下的 Y 坐标
     * <p>
     * 当 Y 不出现，则采用上一个 TextCode 的 Y 值，text object中的一个
     * TextCode attribute is required
     *
     * @param y 第一个文字的glyph原点在对象坐标系下的 Y 坐标
     * @return this
     */
    public TextCode setY(Double y) {
        if (y == null) {
            this.removeAttr("Y");
            return this;
        }
        this.addAttribute("Y", STBase.fmt(y));
        return this;
    }

    /**
     * [optional attribute]
     * 设置 第一个文字的glyph在对象坐标系下的 Y 坐标
     * <p>
     * 当 X 不出现，则采用上一个 TextCode 的 Y 值，text object中的一个
     * TextCode attribute is required
     *
     * @return 第一个文字的glyph在对象坐标系下的 Y 坐标；null表示采用上一个 TextCode 的 Y 值
     */
    public Double getY() {
        String str = this.attributeValue("Y");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional attribute]
     * set offset between characters in the X direction
     * <p>
     * queue of double values; each value represents the spacing between a character and the previous one
     * offset value between characters in the X direction
     * <p>
     * when DeltaX is absent, the text drawing point has no X-direction offset.
     *
     * @param deltaX 文字之间在 X 方向上的偏移值
     * @return this
     */
    public TextCode setDeltaX(ST_Array deltaX) {
        if (deltaX == null) {
            this.removeAttr("DeltaX");
            return this;
        }
        this.addAttribute("DeltaX", deltaX.toString());
        return this;
    }

    /**
     * [optional attribute]
     * set offset between characters in the X direction
     * <p>
     * queue of double values; each value represents the spacing between a character and the previous one
     * offset value between characters in the X direction
     * <p>
     * when DeltaX is absent, the text drawing point has no X-direction offset.
     *
     * @param arr 文字之间在 X 方向上的偏移值数值
     * @return this
     */
    public TextCode setDeltaX(Double... arr) {
        return setDeltaX(new ST_Array(arr));
    }

    /**
     * [optional attribute]
     * 获取 文字之间在 X 方向上的偏移值
     * <p>
     * queue of double values; each value represents the spacing between a character and the previous one
     * offset value between characters in the X direction
     * <p>
     * when DeltaX is absent, the text drawing point has no X-direction offset.
     *
     * @return 文字之间在 X 方向上的偏移值；null表示不偏移
     */
    public ST_Array getDeltaX() {
        String str = this.attributeValue("DeltaX");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return ST_Array.getInstance(deltaFormatter(str));
    }


    /**
     * [optional attribute]
     * set offset between characters in the Y direction
     * <p>
     * queue of double values; each value represents the spacing between a character and the previous one
     * offset value between characters in the Y direction
     * <p>
     * when DeltaY is absent, the text drawing point has no Y-direction offset.
     *
     * @param deltaY 文字之间在 Y 方向上的偏移值；null表示不偏移
     * @return this
     */
    public TextCode setDeltaY(ST_Array deltaY) {
        if (deltaY == null) {
            this.removeAttr("DeltaY");
            return this;
        }
        this.addAttribute("DeltaY", deltaY.toString());
        return this;
    }

    /**
     * [optional attribute]
     * set offset between characters in the Y direction
     * <p>
     * queue of double values; each value represents the spacing between a character and the previous one
     * offset value between characters in the Y direction
     * <p>
     * when DeltaY is absent, the text drawing point has no Y-direction offset.
     *
     * @param arr 文字之间在 Y 方向上的偏移数值
     * @return this
     */
    public TextCode setDeltaY(Double... arr) {
        return setDeltaY(new ST_Array(arr));
    }

    /**
     * [optional attribute]
     * 获取 文字之间在 Y 方向上的偏移值
     * <p>
     * queue of double values; each value represents the spacing between a character and the previous one
     * offset value between characters in the Y direction
     * <p>
     * when DeltaY is absent, the text drawing point has no Y-direction offset.
     *
     * @return 文字之间在 Y 方向上的偏移值；null表示不偏移
     */
    public ST_Array getDeltaY() {
        String str = this.attributeValue("DeltaY");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return ST_Array.getInstance(deltaFormatter(str));
    }

    /**
     * 解析delta的值，处理g的格式
     * @param delta
     * @return
     */
    private String deltaFormatter(String delta) {
        if(!delta.contains("g")) {
            return delta;
        } else {
            List<String> tempList = Arrays.stream(delta.split(" "))
                    .collect(Collectors.toList());

            boolean gFlag = false;
            boolean gProcessing = false;
            int gItemCount = 0;

            List<String> floatList = new ArrayList<>();
            for (String s : tempList) {
                if ("g".equals(s)) {
                    gFlag = true;
                } else {
                    if (s == null || s.trim().length() == 0) {
                        continue;
                    }
                    if (gFlag) {
                        gItemCount = Integer.parseInt(s);
                        gProcessing = true;
                        gFlag = false;
                    } else if (gProcessing) {
                        for (int j = 0; j < gItemCount; j++) {
                            floatList.add(s);
                        }
                        gProcessing = false;
                    } else {
                        floatList.add(s);
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for (String item : floatList) {
                sb.append(' ').append(item);
            }
            return sb.toString().trim();
        }
    }
}
