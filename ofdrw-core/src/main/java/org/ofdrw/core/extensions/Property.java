package org.ofdrw.core.extensions;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

/**
 * 扩展信息
 * <p>
 * “Name Type Value” 的数值组，用于简单的扩展
 * <p>
 * 17 扩展信息 图 83 表 6
 *
 * @author Quan Guanyu
 * @since 2019-11-20 06:09:35
 */
public class Property extends OFDElement {
    public Property(Element proxy) {
        super(proxy);
    }

    public Property() {
        super("Property");
    }


    public Property(String name, String value, String type) {
        this();
        this.setExtensionName(name)
                .setValue(value)
                .setType(type);
    }

    public Property(String name, String value) {
        this();
        this.setExtensionName(name)
                .setValue(value);
    }


    /**
     * [required attribute]
     * 设置 扩展attribute name称
     *
     * @param name 扩展attribute name称
     * @return this
     */
    public Property setExtensionName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("扩展attribute name称（Name）不能为空");
        }
        this.addAttribute("Name", name);
        return this;
    }

    /**
     * [required attribute]
     * 获取 扩展attribute name称
     *
     * @return 扩展attribute name称
     */
    public String getExtensionName() {
        String str = this.attributeValue("Name");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("扩展attribute name称（Name）不能为空");
        }
        return str;
    }

    /**
     * [optional attribute]
     * 设置 扩展属性值类型
     *
     * @param type 扩展属性值类型
     * @return this
     */
    public Property setType(String type) {
        if (type == null || type.trim().length() == 0) {
            this.removeAttr("Type");
            return this;
        }
        this.addAttribute("Type", type);
        return this;
    }

    /**
     * [optional attribute]
     * 获取 扩展属性值类型
     *
     * @return 扩展属性值类型
     */
    public String getType() {
        return this.attributeValue("Type");
    }


    /**
     * [required]
     * 设置 属性值
     *
     * @param value 属性值
     * @return this
     */
    public Property setValue(String value) {
        if (value == null || value.trim().length() == 0) {
            this.setText("");
            return this;
        }
        this.setText(value);
        return this;
    }

    /**
     * [required]
     * 获取 属性值
     *
     * @return 属性值
     */
    public String getValue() {
        Element e = this.getOFDElement("Value");
        return e == null ? null : e.getTextTrim();
    }
}
