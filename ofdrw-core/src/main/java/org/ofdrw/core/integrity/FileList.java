package org.ofdrw.core.integrity;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Loc;

import java.util.List;

/**
 * GMT0099-2020 D.1 防止夹带文件列表
 *
 * @author Quan Guanyu
 * @since 2021-06-28 19:51:24
 */
public class FileList extends OFDElement {
    public FileList(Element proxy) {
        super(proxy);
    }

    public FileList() {
        super("FileList");
    }

    /**
     * [required]
     * add anti-smuggling file information
     *
     * @param file 防止夹带文件信息
     * @return this
     */
    public FileList addFile(File file) {
        if (file == null) {
            return this;
        }
        this.add(file);
        return this;
    }

    /**
     * [required]
     * add anti-smuggling file information
     *
     * @param id      防止文件标识
     * @param fileLoc 防止file path within the package
     * @return this
     */
    public FileList addFile(String id, String fileLoc) {
        this.add(new File(id, fileLoc));
        return this;
    }

    /**
     * [required]
     * add anti-smuggling file information
     *
     * @param id      防止文件标识
     * @param fileLoc 防止file path within the package
     * @return this
     */
    public FileList addFile(String id, ST_Loc fileLoc) {
        this.add(new File(id, fileLoc));
        return this;
    }


    /**
     * [required]
     * 获取 防止夹带文件信息列表
     *
     * @return 防止夹带文件信息列表
     */
    public List<File> getFiles() {
        return this.getOFDElements("File", File::new);
    }
}
