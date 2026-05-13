package org.ofdrw.pkg.container;

import org.ofdrw.core.annotation.Annotations;

import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * 注释容器
 * <p>
 * GMT0099 OFD 2.0
 *
 * @author Quan Guanyu
 * @since 2021-6-15 19:58:58
 */
public class AnnotsDir extends VirtualContainer {


    public AnnotsDir(Path fullDir) throws IllegalArgumentException {
        super(fullDir);
    }

    /**
     * 设置annotation list object
     *
     * @param annotations annotation list object
     * @return annotation list object
     */
    public AnnotsDir setAnnotations(Annotations annotations) {
        this.putObj(DocDir.AnnotationsFileName, annotations);
        return this;
    }

    /**
     * 通过索引获取 页面文件
     * <p>
     * 如果目录不存在那么创建
     *
     * @param index page index
     * @return page container at specified index
     */
    public PageDir obtainByIndex(int index) {
        String containerName = PageDir.PageContainerPrefix + index;
        return this.obtainContainer(containerName, PageDir::new);
    }

    /**
     * 通过索引获取 页面文件
     * <p>
     * 如果目录不存在那么创建
     *
     * @param containerName 虚拟容器名称
     * @return page container at specified index
     * @throws FileNotFoundException 无法找到指定索引页面
     */
    public PageDir getPageDir(String containerName) throws FileNotFoundException {
        return this.getContainer(containerName, PageDir::new);
    }
}
