package org.ofdrw.layout.engine;

import org.ofdrw.core.text.font.CT_Font;

import java.nio.file.Path;

/**
 * 已经存在的font object
 *
 * @author Quan Guanyu
 * @since 2020-05-10 18:27:27
 */
public class ExistCTFont {

    /**
     * 在文档中的font object
     */
    public CT_Font font;
    /**
     * font fileabsolute path，若为操作系统font可能为空
     */
    public Path absPath;

    public ExistCTFont(CT_Font font, Path absPath) {
        this.font = font;
        this.absPath = absPath;
    }
}
