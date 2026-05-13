package org.ofdrw.converter.font;

import java.awt.geom.GeneralPath;
import java.io.IOException;

/**
 * glyph绘制路径提供器
 *
 * @author Quan Guanyu
 * @since 2021-10-14 20:53:49
 */
@FunctionalInterface
public interface FontDrawPathProvider {

    /**
     * 通过font索引号获取glyph的绘制路径
     *
     * @param gid glyph索引号
     * @return 绘制路径或null（找不到时）
     * @throws IOException 解析异常
     */
    GeneralPath getPath(int gid) throws IOException;
}
