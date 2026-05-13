package org.ofdrw.pkg.container;

import java.nio.file.Path;

/**
 * within the OFD package部文件迭代器
 *
 * @author Quan Guanyu
 * @since 2021-08-24 20:37:50
 */
@FunctionalInterface
public interface OFDPackageFileIterator {

    /**
     * 变量文件
     *
     * @param pkgAbsPath 包内absolute path
     * @param path       文件系统中路径
     * @return true - 继续遍历；false - 停止遍历
     */
    boolean visit(String pkgAbsPath, Path path);
}
