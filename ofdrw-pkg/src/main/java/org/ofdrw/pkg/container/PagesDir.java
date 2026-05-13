package org.ofdrw.pkg.container;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * page container
 *
 * @author Quan Guanyu
 * @since 2020-01-18 03:34:34
 */
public class PagesDir extends VirtualContainer {
    /**
     * 最大page index + 1
     * <p>
     * index + 1
     */
    private int maxPageIndex = 0;


    public PagesDir(Path fullDir) throws IllegalArgumentException {
        super(fullDir);
        initContainer();
    }

    /**
     * 初始化容器
     */
    private void initContainer() {
        File fullDirFile = new File(getSysAbsPath());
        File[] files = fullDirFile.listFiles();
        if (files != null) {
            // 遍历容器中已经有的页面目录，初始页面数量
            for (File f : files) {
                // 签名目录名为： Page_N
                if (f.getName().startsWith(PageDir.PageContainerPrefix)) {
                    String numb = f.getName().replace(PageDir.PageContainerPrefix, "");
                    try{
                        int num = Integer.parseInt(numb);
                        if (maxPageIndex <= num) {
                            maxPageIndex = num + 1;
                        }
                    } catch (NumberFormatException e){
                        // ignore
                    }
                }
            }
        }
    }

    /**
     * 创建一个新的page container
     *
     * @return page container
     */
    public PageDir newPageDir() {
        String name = PageDir.PageContainerPrefix + maxPageIndex;
        maxPageIndex++;
        // create container
        return this.obtainContainer(name, PageDir::new);
    }

    /**
     * 获取索引的page container
     * <p>
     * page number = index + 1
     *
     * @param index 索引（从0开始）
     * @return page container at specified index
     * @throws FileNotFoundException 无法找到指定索引页面
     */
    public PageDir getByIndex(int index) throws FileNotFoundException {
        String containerName = PageDir.PageContainerPrefix + index;
        return this.getContainer(containerName, PageDir::new);
    }
    public PageDir getPageDir(String containerName) throws FileNotFoundException {
        return this.getContainer(containerName, PageDir::new);
    }
}
