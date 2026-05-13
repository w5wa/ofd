package org.ofdrw.core;

import java.io.Serializable;

/**
 * 简单类型element object，用于承载 Text
 *
 * @author Quan Guanyu
 * @since 2019-10-01 03:22:42
 */
public class OFDSimpleTypeElement extends OFDElement {

    /**
     * 创建一个带有文本元素
     *
     * @param name element name
     * @param obj  元素值对象（可toString 序列化为string）
     */
    public OFDSimpleTypeElement(String name, Serializable obj) {
        super(name);
        this.setText(obj.toString());
    }

    @Override
    public String getQualifiedName() {
        return this.proxy.getQualifiedName();
    }
}
