package org.ofdrw.core;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.QName;
import org.ofdrw.core.basicType.ST_ID;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * file root node
 * <p>
 * XML document使用的namespace为 http://www.ofdspec.org/2016，其表示符应为 ofd；
 * 应在XML documents within the package的root node declaration defaults:ofd。
 * element node应使用namespace identifier，element attribute不使用namespace identifier。
 * — GB/T 33190-2016, Section 7.1: Namespaces
 *
 * @author Quan Guanyu
 * @since 2019-09-28 12:05:55
 */
public class OFDElement extends DefaultElementProxy {

    /**
     * namespace strict mode
     * <p>
     * true - strictly useOFD namespace获取OFD element
     * <p>
     * false - as long as the element name is the same则considered an OFD element（默认值）
     */
    public static boolean NSStrictMode = false;

    public OFDElement(Element proxy) {
        super(proxy);
    }

    protected OFDElement(String name) {
        // 设置 xmlns:ofd=http://www.ofdspec.org/2016 , 并增加 ofd
        super(name, Const.OFD_NAMESPACE);
    }

    /**
     * @param name element name
     * @return 获取OFD type elementinstance
     */
    public static OFDElement getInstance(String name) {
        return new OFDElement(name);
    }

    /**
     * 向in elementadd OFD element
     *
     * @param name  element name
     * @param value element text content
     * @return this
     */
    public OFDElement addOFDEntity(String name, Serializable value) {
        this.add(new OFDSimpleTypeElement(name, value));
        return this;
    }

    /**
     * 设置OFD参数
     * <p>
     * 如果if parameter exists, modify it
     * <p>
     * 如果need to delete element，请使用 {@link #removeOFDElemByNames}
     *
     * @param name  element name
     * @param value element text content
     * @return this
     */
    public OFDElement setOFDEntity(String name, Serializable value) {
        Element e = this.getOFDElement(name);
        if (e == null) {
            return addOFDEntity(name, value);
        } else {
            e.setText(value.toString());
            return this;
        }
    }

    /**
     * 设置 element name
     *
     * @param name element name
     * @return this
     */
    public OFDElement setOFDName(String name) {
        this.setQName(new QName(name, Const.OFD_NAMESPACE));
        return this;
    }

    /**
     * 获取OFD element
     * <p>
     * 若无法在OFD namespace下get element with same name，则尝试从default namespace获取。
     *
     * @param name OFDelement name
     * @return OFD element or null
     */
    public Element getOFDElement(String name) {
        Element res;
        if (NSStrictMode) {
            res = this.element(new OFDCommonQName(name));
        } else {
            res = this.element(name);
        }
        return res;
    }


    /**
     * proxy object creation
     *
     * @param name   element name
     * @param mapper proxy object constructor
     * @param <R>    element type
     * @return proxy object
     */
    public <R extends OFDElement> R getOFDElement(String name, Function<? super Element, ? extends R> mapper) {
        Element e = this.getOFDElement(name);
        if (e == null) {
            return null;
        }
        return mapper.apply(e);
    }

    /**
     * 如果属性存在则删除
     *
     * @param name attribute name
     * @return true deleted; false = deletion failed，可能是由于attribute does not exist
     */
    public boolean removeAttr(String name) {
        Attribute a = this.attribute(name);
        if (a != null) {
            return this.remove(a);
        }
        return false;
    }

    /**
     * 获取text content of OFD element
     *
     * @param name element name
     * @return text content
     */
    public String getOFDElementText(String name) {
        Element element = getOFDElement(name);
        return element == null ? null : element.getText();
    }

    /**
     * 获取 OFD element collection with specified name
     * <p>
     * 集合将会保持原有次序
     * <p>
     * 若容器内的element not in OFD namespace，但是names are the same也会被取到
     *
     * @param name   OFDelement name
     * @param mapper converter object constructor reference
     * @param <R>    指定element object
     * @return OFD element collection with specified name
     */
    public <R> List<R> getOFDElements(String name, Function<? super Element, ? extends R> mapper) {
        List<Element> c;

        if (NSStrictMode) {
            c = this.elements(new OFDCommonQName(name));
        } else {
            c = this.elements(name);
        }

        if (c == null || c.isEmpty()) {
            return Collections.emptyList();
        }
        return c.stream().map(mapper).collect(Collectors.toList());
    }


    /**
     * 获取 OFD element collection with specified name
     * <p>
     * 集合将会保持原有次序
     * qname匹配的时候不再验证namespace，兼容namespace为空的情况。
     *
     * @param name   OFDelement name
     * @param mapper converter object constructor reference
     * @param <R>    指定element object
     * @return OFD element collection with specified name
     * @deprecated {@link #getOFDElements}已经兼容非标准namespace
     */
    @Deprecated
    public <R> List<R> getElements(String name, Function<? super Element, ? extends R> mapper) {
        List<Element> elements = this.elements(new QName(name));
        if (elements == null || elements.isEmpty()) {
            return Collections.emptyList();
        }
        return elements.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 设置元素
     * <p>
     * 如果同类型元素已经存在，那么删除原有元素
     *
     * @param element 需要设置的元素
     * @return this
     */
    public OFDElement set(Element element) {
        if (element == null) {
            return this;
        }
        // 删除原有内容
        List<Element> old = this.elements(element.getQName());
        if (old != null && !old.isEmpty()) {
            for (Element toBeReplace : old) {
                this.remove(toBeReplace);
            }
        }
        this.add(element);
        return this;
    }

    /**
     * 根据 OFD element的名称删除节点内所有匹配的OFD element
     *
     * @param names 需要被删除element name序列
     * @return 被删除的所有OFD element
     */
    public List<Element> removeOFDElemByNames(String... names) {
        List<Element> deleteElements = new LinkedList<>();
        if (names == null) {
            return null;
        }
        // 遍历所有需要被删除的名称
        for (String name : names) {
            if (name == null || name.trim().length() == 0) {
                continue;
            }
            // 根据名字获取指定类型的元素
            for (Element toBeDelete : this.getOFDElements(name, OFDElement::new)) {
                if (toBeDelete == null) {
                    continue;
                }
                this.remove(toBeDelete);
                deleteElements.add(toBeDelete);
            }
        }
        return deleteElements;
    }


    /**
     * [optional]
     * <p>
     * set OFD object identifier, an unsigned integer that must be unique within the document.
     * <p>
     * 0 indicates invalid identifier
     *
     * @param objId OFD对象标识
     * @return this
     */
    public OFDElement setObjID(ST_ID objId) {
        this.addAttribute("ID", objId.toString());
        return this;
    }

    public OFDElement setObjID(long objId) {
        return this.setObjID(new ST_ID(objId));
    }

    /**
     * [optional]
     * <p>
     * set OFD object identifier, an unsigned integer that must be unique within the document.
     *
     * @param id OFD对象标识，请确保id为numberstring
     * @return this
     */
    public OFDElement setObjID(String id) {
        this.addAttribute("ID", id);
        return this;
    }

    /**
     * 移除in element所有内容
     *
     * @return this
     */
    public OFDElement removeAll() {
        this.elements().forEach(this::remove);
        return this;
    }

    /**
     * [optional]
     * <p>
     * set OFD object identifier, an unsigned integer that must be unique within the document.
     * <p>
     * 0 indicates invalid identifier
     *
     * @return OFD对象标识，null表示对象标识不存在
     */
    public ST_ID getObjID() {
        return ST_ID.getInstance(this.attributeValue("ID"));
    }


    /**
     * OFD element采用OFD namespace，所以直接调用proxy object
     *
     * @return fully qualified element name (with prefix)
     */
    @Override
    public String getQualifiedName() {
        return this.proxy.getQualifiedName();
    }
}
