package org.ofdrw.crypto;

import java.nio.file.Path;

/**
 * OFD容器文件过滤
 *
 * @author Quan Guanyu
 * @since 2021-07-15 18:39:06
 */
@FunctionalInterface
public interface ContainerFileFilter {

    /**
     * 执行过滤
     *
     * @param containerPath 文件在容器内的路径
     * @param absPath       文件absolute path
     * @return true - 保留; false - ignored
     */
    boolean filter(String containerPath, Path absPath);
}
