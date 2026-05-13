package org.ofdrw.core.basicStructure.pageObj.layer;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.pageObj.layer.block.CT_PageBlock;
import org.ofdrw.core.basicType.ST_RefID;

public class CT_Layer extends CT_PageBlock {
    public CT_Layer(Element proxy) {
        super(proxy);
    }

    public CT_Layer() {
        super("Layer");
    }

    /**
     * [optional attribute]
     * 设置 层类型描述
     * <p>
     * 默认值为 Body
     *
     * @param type 层类型
     * @return this
     */
    public CT_Layer setType(Type type) {
        if (type == null) {
            this.removeAttr("Type");
            return this;
        }
        this.addAttribute("Type", type.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 层类型描述
     * <p>
     * 默认值为 Body
     *
     * @return 层类型
     */
    public Type getType() {
        Type type = Type.getInstance(this.attributeValue("Type"));
        return type == null ? Type.Body : type;
    }

    /**
     * [optional attribute]
     * 设置 layer的drawing parameters，引用resource file总定义的drawing parameters标识
     *
     * @param drawParam resource file总定义的drawing parameters标识
     * @return this
     */
    public CT_Layer setDrawParam(ST_RefID drawParam) {
        if (drawParam == null) {
            this.removeAttr("DrawParam");
            return this;
        }
        this.addAttribute("DrawParam", drawParam.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 layer的drawing parameters，引用resource file总定义的drawing parameters标识
     *
     * @return resource file总定义的drawing parameters标识，null表示不存在
     */
    public ST_RefID getDrawParam() {
        return ST_RefID.getInstance(this.attributeValue("DrawParam"));
    }
}
