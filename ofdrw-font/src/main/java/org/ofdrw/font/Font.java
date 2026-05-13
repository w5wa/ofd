package org.ofdrw.font;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * font
 *
 * @author Quan Guanyu
 * @since 2020-02-03 02:04:29
 */
public class Font {
    /**
     * font name
     */
    private String name;

    /**
     * font族名称
     */
    private String familyName;

    /**
     * fontfile path
     */
    private Path fontFile;

    /**
     * 可打印字符width映射
     */
    private double[] printableAsciiWidthMap = null;
    /**
     * 缓存的AWTfont
     */
    private java.awt.Font fontObj;

    /**
     * 是否可嵌入OFD file包
     */
    private boolean embeddable = true;

    private Font() {
    }

    /**
     * @return 默认font（Noto思源宋体）
     */
    public static Font getDefault() {
        return FontName.SimSun.font();
    }

    public Font(String name, String familyName, Path fontFile) {
        this.name = name;
        this.familyName = familyName;
        if (fontFile == null || Files.notExists(fontFile)) {
            throw new IllegalArgumentException("font file(fontFile)不存在");
        }
        this.setFontFile(fontFile);
    }

    public Font(String name, String familyName) {
        this.name = name;
        this.familyName = familyName;
    }

    public Font(String name, Path fontFile) {
        this.name = name;
        if (fontFile == null || Files.notExists(fontFile)) {
            throw new IllegalArgumentException("font file(fontFile)不存在");
        }
        this.setFontFile(fontFile);

    }

    /**
     * 创建font并指定 可打印字符width缩放倍数
     *
     * @param name                   glyph name
     * @param familyName             字族名
     * @param fontFile               glyphfile path
     * @param printableAsciiWidthMap 可打印字符映射表，用于处理字符的width
     */
    public Font(String name, String familyName, Path fontFile, double[] printableAsciiWidthMap) {
        this.name = name;
        this.familyName = familyName;
        this.fontFile = fontFile;
        this.printableAsciiWidthMap = printableAsciiWidthMap;
    }

    /**
     * font是否存在预设的字符width映射表
     *
     * @return true - 存在；false - 不存在
     */
    public boolean hasWidthMath() {
        return this.printableAsciiWidthMap != null && this.printableAsciiWidthMap.length > 0;
    }

    /**
     * 获取字符占比
     *
     * @param txt 字符
     * @return 0~1 占比
     */
    public double getCharWidthScale(char txt) {
        // 如果存在字符映射那么从字符映射中获取width占比
        if (printableAsciiWidthMap != null) {
            // 所有 ASCII码均采用半角
            if (txt >= 32 && txt <= 126) {
                // 根据可打印width比例映射表打印
                return printableAsciiWidthMap[txt - 32];
            } else {
                // 非英文字符
                return 1;
            }
        } else {
            // 不存在字符映射，那么认为是等width比例 ASCII 为 0.5 其他为 1
            return (txt >= 32 && txt <= 126) ? 0.5 : 1;
        }
    }

    /**
     * 设置可打印字符width映射表
     * <p>
     * 在使用操作系统font时，默认采用ACSII 0.5 其余1的比例计算width，因此可能需要手动设置width比例才可以达到相应的效果
     *
     * @param map 映射比例表
     * @return this
     */
    public Font setPrintableAsciiWidthMap(double[] map) {
        this.printableAsciiWidthMap = map;
        return this;
    }

    /**
     * 获取font全名
     *
     * @return font全名
     */
    public String getCompleteFontName() {
        if (familyName == null) {
            return name;
        } else {
            return name + "-" + familyName;
        }
    }

    public String getName() {
        return name;
    }

    public Font setName(String name) {
        this.name = name;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Font setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public Path getFontFile() {
        return fontFile;
    }

    /**
     * 获取fontfile name
     *
     * @return fontfile name
     */
    public String getFontFileName() {
        return fontFile.getFileName().toString();
    }

    /**
     * 设置font file
     *
     * @param fontFile font file
     * @return this
     */
    public Font setFontFile(Path fontFile) {
        this.fontFile = fontFile;
        try (InputStream in = Files.newInputStream(fontFile)) {
            this.fontObj = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
            throw new IllegalArgumentException("font file(fontFile)格式错误");
        }
        return this;
    }

    /**
     * 获取AWTfont object，该对象可能为空（当前没有提供font路径时为空）
     *
     * @return font object 或  null
     */
    public java.awt.Font getFontObj() {
        return this.fontObj;
    }

    /**
     * 获取font嵌入标识
     *
     * @return true-代表font file会被嵌入到OFD file中，false-表示不嵌入
     */
    public boolean isEmbeddable() {
        return embeddable;
    }

    /**
     * 是否可嵌入OFD file包。 如果设置为false,则font file仅在生成OFD document的过程中被引用，不会包contains到OFD file内。
     * 默认值为true。
     *
     * @param embeddable true-嵌入，false-不嵌入
     * @return this
     */
    public Font setEmbeddable(boolean embeddable) {
        this.embeddable = embeddable;
        return this;
    }
}
