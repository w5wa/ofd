package org.ofdrw.layout.element;

import org.ofdrw.font.Font;

/**
 * 文本font信息
 *
 * @author Quan Guanyu
 * @since 2020-05-07 19:18:43
 */
public interface TextFontInfo {
    /**
     * 获取font object
     * @return font object
     */
    Font getFont();

    /**
     * 获取字间距
     * @return 字间距，单位毫米mm
     */
    Double getLetterSpacing();

    /**
     * 获取文字字号
     * @return 字号，单位毫米mm
     */
    Double getFontSize();
}
