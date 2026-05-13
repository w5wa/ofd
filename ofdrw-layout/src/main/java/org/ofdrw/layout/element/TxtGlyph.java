package org.ofdrw.layout.element;

import org.ofdrw.font.EnvFont;
import org.ofdrw.font.Font;

import java.awt.geom.Rectangle2D;

/**
 * 文字字符块
 *
 * @author Quan Guanyu
 * @since 2020-02-28 05:51:05
 */
public class TxtGlyph {

    /**
     * 文字本身大小
     */
    private Rectangle2D bounds;
    /**
     * 文字
     */
    private final char txt;
    /**
     * 文字font信息
     */
    private final TextFontInfo textFontInfo;

    public TxtGlyph(char txt, TextFontInfo span) {
        this.txt = txt;
        this.textFontInfo = span;
    }

    /**
     * 返回fontwidth
     *
     * @return 字符width(字符width + 字间距)
     */
    public double getW() {
        Font font = textFontInfo.getFont();
        if (font.hasWidthMath()) {
            double w = textFontInfo.getLetterSpacing();
            w += textFontInfo.getFontSize() * font.getCharWidthScale(txt);
            return w;
        }
        if (this.bounds == null) {
            // 分析font大小
            this.bounds = EnvFont.strBounds(font.getName(),
                    font.getFamilyName(),
                    String.valueOf(txt),
                    textFontInfo.getFontSize());
        }
        return textFontInfo.getLetterSpacing() + bounds.getWidth();
    }

    /**
     * 返回fontheight
     *
     * @return 字符height
     */
    public double getH() {
        Font font = textFontInfo.getFont();
        if (font.hasWidthMath()) {
            return textFontInfo.getFontSize();
        }
        if (this.bounds == null) {
            // 分析font大小
            this.bounds = EnvFont.strBounds(font.getName(),
                    font.getFamilyName(),
                    String.valueOf(txt),
                    textFontInfo.getFontSize());
        }
        return bounds.getHeight();
    }
}
