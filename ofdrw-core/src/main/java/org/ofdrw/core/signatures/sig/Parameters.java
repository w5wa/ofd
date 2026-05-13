package org.ofdrw.core.signatures.sig;

import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ofdrw.core.OFDElement;

import java.util.List;

/**
 * OFD 2.0 GMT0099 B.2
 * <p>
 * 签名扩展属性集
 *
 * @author Quan Guanyu
 * @since 2021-06-19 12:53:53
 */
public class Parameters extends OFDElement {
    public Parameters(Element proxy) {
        super(proxy);
    }

    public Parameters() {
        super("Parameters");
    }

    /**
     * [required, OFD 2.0]
     * add signature extension attribute
     *
     * @param parameter 签名扩展属性
     * @return this
     */
    public Parameters addParameter(@Nullable Parameter parameter) {
        if (parameter == null) {
            return this;
        }
        String name = parameter.getNameAttr();
        final Parameter oldP = getParameter(name);
        if (oldP != null) {
            // 对应名称的签名属性已经存在，那么就删除原有属性
            this.remove(oldP);
        }
        this.add(parameter);
        return this;
    }

    /**
     * [required, OFD 2.0]
     * add signature extension attribute
     *
     * @param name  attribute name
     * @param value attribute value
     * @return this
     */
    public Parameters addParameter(@NotNull String name, String value) {
        final Parameter parameter = new Parameter(name,value);
        return this.addParameter(parameter);
    }
    /**
     * [required, OFD 2.0]
     * add signature extension attribute
     *
     * @param name  attribute name
     * @param type  属性的类型
     * @param value attribute value
     * @return this
     */
    public Parameters addParameter(@NotNull String name, String type, String value) {
        final Parameter parameter = new Parameter(name, type, value);
        return this.addParameter(parameter);
    }

    /**
     * 通过 attribute name删除 签名扩展属性
     *
     * @param name 签名扩展attribute name称
     * @return 被删除的属性 或 null
     */
    @Nullable
    public Parameter removeParameter(@Nullable String name) {
        if (name == null) {
            return null;
        }
        Parameter oldP = getParameter(name);
        if (oldP != null) {
            this.remove(oldP);
        }
        return oldP;
    }

    /**
     * 通过attribute name称获取签名扩展属性
     *
     * @param name 签名扩展attribute name称
     * @return 签名扩展属性，或null
     */
    @Nullable
    public Parameter getParameter(@NotNull String name) {
        final List<Parameter> pList = getParameters();
        for (Parameter p : pList) {
            if (name.equals(p.getNameAttr())) {
                return p;
            }
        }
        return null;
    }

    /**
     * [required, OFD 2.0]
     * 获取 所有签名扩展属性
     *
     * @return 签名扩展属性列表
     */
    public List<Parameter> getParameters() {
        return this.getOFDElements("Parameter", Parameter::new);
    }
}
