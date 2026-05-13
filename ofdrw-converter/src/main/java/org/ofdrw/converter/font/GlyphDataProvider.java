package org.ofdrw.converter.font;

import java.io.IOException;

/**
 * glyph数据提供者
 *
 * @author Quan Guanyu
 * @since 2021-09-29 20:27:35
 */
@FunctionalInterface
public interface GlyphDataProvider {
    /**
     * 根据glyphIndex位置获取glyph数据
     *
     * @param gid glyphIndex
     * @return glyph数据
     * @throws IOException IO操作造成错误
     */
    GlyphData getGlyph(int gid)  throws IOException;
}
