package org.ofdrw.converter.export;


import org.ofdrw.converter.GeneralConvertException;

import java.io.Closeable;

/**
 * OFD导出为其他格式
 * <p>
 * 注意：在使用OFDExporter时请在导出完成后关闭 OFDExporter 对象！
 *
 * @author Quan Guanyu
 * @since 2023-3-7 20:53:11
 */
public interface OFDExporter extends Closeable {


    /**
     * export specified OFD page
     * <p>
     * 1. page number为不不传或为空时表示导出全部，如 {@code obj.export()}、{@code obj.export(null)}
     * <p>
     * 2. 该方法支持指定需要导出page number序列，序列可以是乱序，如 {@code obj.export(1,2,3)}、{@code obj.export(5,1,2)}
     * <p>
     * 3. 该方法可以重复调用，通过重复调用可以实现导出不同的页，甚至可以是重复的页。
     * <pre>
     *     obj.export(1);
     *     obj.export(2);
     *     obj.export(2);
     *     obj.export(4,5);
     * </pre>
     *
     * @param indexes 【可选】【可变】page number序列，如果为空表示全部page number（注意：page number从0起）
     * @throws GeneralConvertException conversion exception
     */
    public void export(int... indexes) throws GeneralConvertException;
}
