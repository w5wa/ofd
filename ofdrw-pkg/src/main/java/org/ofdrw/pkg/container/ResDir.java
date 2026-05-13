package org.ofdrw.pkg.container;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 资源目录
 *
 * @author Quan Guanyu
 * @since 2020-4-3 19:41:32
 */
public class ResDir extends VirtualContainer {

    public ResDir(Path fullDir) throws IllegalArgumentException {
        super(fullDir);
    }

    /**
     * 向目录中加入资源
     * <p>
     * 加入的资源将会被复制到指定目录，与原有资源无关。
     * <p>
     * 若存在同名文件，那么被加入文件将重命名，若你需要获取重名后的文件信息请使用 {@link #putFileWithPath(Path)}
     *
     * @param res resource
     * @return this
     * @throws IOException exception during file copy
     */
    public ResDir add(Path res) throws IOException {
        if (res == null || Files.notExists(res)) {
            return this;
        }
        this.putFile(res);
        return this;
    }

    /**
     * 向目录中加入资源
     * <p>
     * 加入的资源将会被复制到指定目录，与原有资源无关
     * <p>
     * 若存在同名文件，那么被加入文件将重命名，重名后的文件以返还值形式返回。
     *
     * @param res resource
     * @return 加入后文件在容器的absolute path
     * @throws IOException exception during file copy
     */
    public Path addWithPath(Path res) throws IOException {
        if (res == null || Files.notExists(res)) {
            return null;
        }
        return this.putFileWithPath(res);
    }


    /**
     * 获取容器中的资源
     *
     * @param name 资源名称（contains后缀）
     * @return 容器中的resource path
     * @throws FileNotFoundException file not found
     */
    public Path get(String name) throws FileNotFoundException {
        return getFile(name);
    }
}
