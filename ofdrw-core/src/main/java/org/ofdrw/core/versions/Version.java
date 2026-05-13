package org.ofdrw.core.versions;

import org.dom4j.Element;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.OFDElement;

/**
 * 表 70 版本描述入口
 *
 * @author Quan Guanyu
 * @since 2019-09-29 10:40:02
 */
public class Version extends OFDElement {

    public Version(Element proxy) {
        super(proxy);
    }


    public Version() {
        super("Version");
    }

    /**
     * 创建版本描述入口
     *
     * 默认为默认版本（Current=“false”）
     * @param id 版本标识（不含特殊字符string）
     * @param index 版本号
     * @param baseLoc points to包内的版本描述文件
     */
    public Version(String id, int index, ST_Loc baseLoc) {
        this();
        this.setID(id)
                .setIndex(index)
                .setBaseLoc(baseLoc);
    }

    /**
     * [required]
     * 设置版本标识
     *
     * @param id 版本标识 （不含特殊字符，string）
     * @return this
     */
    public Version setID(String id) {
        this.addAttribute("ID", id);
        return this;
    }

    /**
     * [required]
     * 获取版本标识
     * @return 版本标识
     */
    public String getID() {
        return this.attributeValue("ID");
    }

    /**
     * [required]
     * 设置 版本号
     *
     * @param index 版本号
     * @return this
     */
    public Version setIndex(int index) {
        this.addAttribute("Index", index + "");
        return this;
    }

    /**
     * [required]
     * 获取 版本号
     *
     * @return 版本号， null表示没有
     */
    public Integer getIndex() {
        String value = this.attributeValue("Index");
        return value == null ? null : Integer.valueOf(value);
    }

    /**
     * [optional]
     * 获取 是否是默认版本
     * <p>
     * default value: false
     *
     * @return true 表示是默认版本
     */
    public boolean getCurrent() {
        String value = this.attributeValue("Current");
        return Boolean.parseBoolean(value);
    }

    /**
     * [optional]
     * 设置 是否是默认版本
     *
     * @param current true 表示是默认版本
     * @return this
     */
    public Version setCurrent(boolean current) {
        this.addAttribute("Current", current + "");
        return this;
    }

    /**
     * [required]
     * 设置 points to包内的版本描述文件
     *
     * @param baseLoc 版本描述file path
     * @return this
     */
    public Version setBaseLoc(ST_Loc baseLoc) {
        this.addAttribute("BaseLoc", baseLoc.toString());
        return this;
    }

    /**
     * [required]
     * 设置 points to包内 的版本描述文件
     *
     * @return 版本描述file path
     */
    public ST_Loc getBaseLoc() {
        String value = this.attributeValue("BaseLoc");
        return value == null ? null : new ST_Loc(value);
    }

    @Override
    public String getQualifiedName() {
        return "ofd:Version";
    }
}
