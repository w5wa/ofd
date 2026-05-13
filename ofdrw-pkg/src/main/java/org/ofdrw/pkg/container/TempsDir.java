package org.ofdrw.pkg.container;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.ofdrw.core.Holder;
import org.ofdrw.core.basicStructure.pageObj.Page;
import org.ofdrw.core.basicType.ST_Loc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 模板文件存放目录
 *
 * @author Quan Guanyu
 * @since 2020-4-3 19:41:32
 */
public class TempsDir extends VirtualContainer {

    public static final String TempFilePrefix = "Temp_";

    public static final Pattern TempFileNameRegex = Pattern.compile("Temp_(\\d+).xml");

    private int maxTempIndex = -1;

    public TempsDir(Path fullDir) throws IllegalArgumentException {
        super(fullDir);
    }

    /**
     * 向目录中加入模板文件
     * <p>
     * 加入的资源将会被复制到指定目录，与原有文件
     *
     * @param res resource
     * @return this
     * @throws IOException exception during file copy
     */
    public TempsDir add(Path res) throws IOException {
        if (Files.notExists(res)) {
            return this;
        }
        this.putFile(res);
        return this;
    }

    /**
     * 向目录中加入模板页面
     *
     * @param fileName 模板file name
     * @param page     模板页面
     * @return 加入页面的容器内absolute path
     */
    public ST_Loc add(String fileName, Page page) {
        this.putObj(fileName, page);
        return this.getAbsLoc().cat(fileName);
    }

    /**
     * 向容器内加入模板
     *
     * @param page 模板页面
     * @return 模板的容器内absolute path
     * @throws IOException file read/write exception
     */
    public ST_Loc add(Page page) throws IOException {
        if (page == null) {
            return null;
        }
        this.maxTempIndex = getMaxTempIndex() + 1;
        String fileName = String.format("%s%d.xml", TempFilePrefix, maxTempIndex);
        return add(fileName, page);
    }

    /**
     * 根据filename获取模板page object
     *
     * @param fileName filename
     * @return 模板原页面
     * @throws DocumentException     文档无法解析
     * @throws FileNotFoundException file not found
     */
    public Page get(String fileName) throws DocumentException, FileNotFoundException {
        final Element element = this.getObj(fileName);
        return new Page(element);
    }

    /**
     * 获取当前容器内最大的模板文件索引号
     *
     * @return 索引number
     * @throws IOException file read exception
     */
    public Integer getMaxTempIndex() throws IOException {
        if (maxTempIndex < 0) {
            Holder<Integer> maxIndexHolder = new Holder<>(-1);
            try (Stream<Path> stream = Files.list(this.getContainerPath())) {
                stream.forEach((item) -> {
                    String fileName = item.getFileName().toString().toLowerCase();
                    // not a directory and filename starts with Annot_
                    if (fileName.startsWith(TempFilePrefix.toLowerCase())) {
                        String numStr = fileName.replace(TempFilePrefix.toLowerCase(), "")
                                .split("\\.")[0];
                        try {
                            int n = Integer.parseInt(numStr);
                            if (n > maxIndexHolder.value) {
                                maxIndexHolder.value = n;
                            }
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                    }
                });
            }
            maxTempIndex = maxIndexHolder.value;
        }
        return maxTempIndex;
    }
}
