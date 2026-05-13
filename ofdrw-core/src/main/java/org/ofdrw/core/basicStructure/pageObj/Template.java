package org.ofdrw.core.basicStructure.pageObj;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.pageObj.layer.Type;
import org.ofdrw.core.basicType.ST_RefID;

/**
 * 页面使用的模板页
 * <p>
 * template page content and structure are the same as normal pages, defined in CommonData
 * in the specified XML file. A page can use multiple template pages. This node
 * references the specific template via TemplateID and uses ZOrder
 * attribute to control the display order of the template on the page.
 * <p>
 * Note: this attribute is invalid in template page content descriptions.
 *
 * @author Quan Guanyu
 * @since 2019-10-09 09:44:37
 */
public class Template extends OFDElement {
    public Template(Element proxy) {
        super(proxy);
    }

    public Template() {
        super("Template");
    }

    /**
     * [required attribute]
     * 设置 引用在文档共用数据（CommonData）中定义的模板标识符
     *
     * @param templateId 引用在文档共用数据（CommonData）中定义的模板标识符
     * @return this
     */
    public Template setTemplateID(ST_RefID templateId) {
        this.addAttribute("TemplateID", templateId.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 引用在文档共用数据（CommonData）中定义的模板标识符
     *
     * @return 引用在文档共用数据（CommonData）中定义的模板标识符
     */
    public ST_RefID getTemplateID() {
        return ST_RefID.getInstance(this.attributeValue("TemplateID"));
    }

    /**
     * [optional attribute]
     * 设置 模板在页面中的呈现顺序
     * <p>
     * 控制模板在页面中的呈现顺序，其类型描述和呈现顺序与Layer中Type的描述和处理一直。
     * <p>
     * 如果多个layer的此属性相同，则应根据其出现的顺序来显示，先出现者先绘制
     * <p>
     * default value: Background
     *
     * @param zOrder 模板在页面中的呈现顺序
     * @return this
     */
    public Template setZOrder(Type zOrder) {
        if (zOrder == null) {
            this.removeAttr("ZOrder");
            return this;
        }
        this.addAttribute("ZOrder", zOrder.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 模板在页面中的呈现顺序
     * <p>
     * 控制模板在页面中的呈现顺序，其类型描述和呈现顺序与Layer中Type的描述和处理一直。
     * <p>
     * 如果多个layer的此属性相同，则应根据其出现的顺序来显示，先出现者先绘制
     * <p>
     * default value: Background
     *
     * @return 模板在页面中的呈现顺序
     */
    public Type getZOrder() {
        Type type = Type.getInstance(this.attributeValue("ZOrder"));
        return type == null ? Type.Background : type;
    }
}
