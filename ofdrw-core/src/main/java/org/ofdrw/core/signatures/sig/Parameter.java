package org.ofdrw.core.signatures.sig;

import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ofdrw.core.OFDElement;

/**
 * 签名扩展属性 OFD 2.0
 * <p>
 * 记录一个属性的“键”（Name）和“值”，值在该节点的内容中记录。
 * <p>
 * GMT0099 B.2
 *
 * @author Quan Guanyu
 * @since 2021-06-19 13:02:59
 */
public class Parameter extends OFDElement {
    public Parameter(Element proxy) {
        super(proxy);
    }

    public Parameter() {
        super("Parameter");
    }

    /**
     * 创建  签名扩展属性
     *
     * @param name  attribute name
     * @param type  属性的类型
     * @param value attribute value
     */
    public Parameter(@NotNull String name, @Nullable String type, @Nullable String value) {
        this();
        setNameAttr(name);
        setType(type);
        setValue(value);
    }

    /**
     * 创建  签名扩展属性
     *
     * @param name  attribute name
     * @param value attribute value
     */
    public Parameter(@NotNull String name, @Nullable String value) {
        this();
        setNameAttr(name);
        setValue(value);
    }

    /**
     * [required attribute, OFD 2.0]
     * 设置 扩展attribute name
     *
     * @param name 扩展attribute name
     * @return this
     */
    public Parameter setNameAttr(@NotNull String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("扩展attribute name（Name）不能为空");
        }
        this.addAttribute("Name", name);
        return this;
    }

    /**
     * [required attribute, OFD 2.0]
     * 获取 扩展attribute name
     *
     * @return 扩展attribute name
     */
    @NotNull
    public String getNameAttr() {
        return this.attributeValue("Name");
    }

    /**
     * [optional attribute, OFD 2.0]
     * 设置 扩展attribute value类型
     *
     * @param type 扩展attribute value类型,null表示删除属性
     * @return this
     */
    public Parameter setType(@Nullable String type) {
        if (type == null) {
            this.removeAttr(type);
            return this;
        }
        this.addAttribute("Type", type);
        return this;
    }

    /**
     * [optional attribute, OFD 2.0]
     * 获取 扩展attribute value类型
     *
     * @return 扩展attribute value类型，可能为空
     */
    @Nullable
    public String getType() {
        return this.attributeValue("Type");
    }

    /**
     * [optional, OFD 2.0]
     * 设置 签名扩展属性值
     *
     * @param value 签名扩展属性值
     * @return this
     */
    public Parameter setValue(@Nullable String value) {
        if (value == null) {
            value = "";
        }
        this.setText(value);
        return this;
    }

    /**
     * [optional, OFD 2.0]
     * 设置 签名扩展属性值
     *
     * @return 签名扩展属性值，或空串
     */
    @NotNull
    public String getValue() {
        return this.getTextTrim();
    }
}
