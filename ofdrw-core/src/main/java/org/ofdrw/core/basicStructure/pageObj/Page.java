package org.ofdrw.core.basicStructure.pageObj;

import org.dom4j.Element;
import org.ofdrw.core.Const;
import org.ofdrw.core.action.Actions;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.doc.CT_PageArea;
import org.ofdrw.core.basicType.ST_Loc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 页对象
 * <p>
 * 页对象支持模板页描述，每一页经常要重复显示的内容可统一在模板页中描述，
 * 文档可以contains多个模板页。通过使用模板页可以使重复显示的内容不必出现在
 * 描述每一页的页面描述内容中，而只需通过 Template 节点进行应用。
 * <p>
 * 7.7 图 13 页对象结构；表 12 页对象属性
 *
 * @author Quan Guanyu
 * @since 2019-10-09 09:38:35
 */
public class Page extends OFDElement {

    public Page(Element proxy) {
        super(proxy);
    }

    public Page() {
        super("Page");
    }


    /**
     * [optional]
     * 设置 page area的大小和位置，仅对该页面有效。
     * <p>
     * when this node is absent, use template page definition; if no template page
     * or no page area is defined there, use the CommonData definition.
     *
     * @param area page area的大小和位置
     * @return this
     */
    public Page setArea(CT_PageArea area) {
        area.setOFDName("Area");
        this.set(area);
        return this;
    }

    /**
     * [optional]
     * 获取 page area的大小和位置，仅对该页面有效。
     * <p>
     * when this node is absent, use template page definition; if no template page
     * or no page area is defined there, use the CommonData definition.
     *
     * @return page area的大小和位置
     */
    public CT_PageArea getArea() {
        Element e = this.getOFDElement("Area");
        return e == null ? null : new CT_PageArea(e);
    }


    /**
     * [optional]
     * 设置 页面使用的模板页
     * <p>
     * template page content and structure are the same as normal pages, defined in CommonData
     * in the specified XML file. A page can use multiple template pages. This node
     * references the specific template via TemplateID and uses ZOrder
     * attribute to control the display order of the template on the page.
     * <p>
     * Note: this attribute is invalid in template page content descriptions.
     *
     * @param template 页面使用的模板页
     * @return this
     */
    public Page addTemplate(Template template) {
        if (template == null) {
            return this;
        }
        this.add(template);
        return this;
    }

    /**
     * @param template 模板
     * @return this
     * @deprecated {@link #addTemplate(Template)}
     */
    @Deprecated
    public Page setTemplate(Template template) {
        this.set(template);
        return this;
    }

    /**
     * [optional]
     * 获取 页面使用的模板页
     * <p>
     * template page content and structure are the same as normal pages, defined in CommonData
     * in the specified XML file. A page can use multiple template pages. This node
     * references the specific template via TemplateID and uses ZOrder
     * attribute to control the display order of the template on the page.
     * <p>
     * Note: this attribute is invalid in template page content descriptions.
     *
     * @return 页面使用的模板页
     */
    public List<Template> getTemplates() {
        return this.getOFDElements("Template", Template::new);
    }

    /**
     * @return 页面使用的模板页(第一个)
     * @deprecated {@link #getTemplates()}
     */
    @Deprecated
    public Template getTemplate() {
        Element e = this.getOFDElement("Template");
        return e == null ? null : new Template(e);
    }


    /**
     * [optional]
     * 设置 页资源
     * <p>
     * points to the resource file used by this page
     *
     * @param pageRes 页resource path
     * @return this
     */
    public Page addPageRes(ST_Loc pageRes) {
        this.addOFDEntity("PageRes", pageRes);
        return this;
    }

    /**
     * [optional]
     * 获取 页资源
     * <p>
     * points to the resource file used by this page
     *
     * @return 页resource path列表
     */
    public List<ST_Loc> getPageResList() {
        return this.getOFDElements("PageRes", e -> new ST_Loc(e.getText()));
    }


    /**
     * [optional]
     * 设置 page content描述，该节点不存在时，标识空白页
     *
     * @param content page content
     * @return this
     */
    public Page setContent(Content content) {
        this.set(content);
        return this;
    }

    /**
     * [optional]
     * 获取 page content描述，该节点不存在时，标识空白页
     *
     * @return page content
     */
    public Content getContent() {
        Element e = this.getOFDElement("Content");
        return e == null ? null : new Content(e);
    }


    /**
     * [optional]
     * set the action sequence associated with the page.
     * <p>
     * when multiple Action objects exist, all actions are executed in order.
     * <p>
     * actions in the action list are associated with the page; event type is PO (page open; see Table 52)
     *
     * @param actions action sequence
     * @return this
     */
    public Page setActions(Actions actions) {
        this.set(actions);
        return this;
    }

    /**
     * [optional]
     * set the action sequence associated with the page.
     * <p>
     * when multiple Action objects exist, all actions are executed in order.
     * <p>
     * actions in the action list are associated with the page; event type is PO (page open; see Table 52)
     *
     * @return action sequence
     */
    public Actions getActions() {
        Element e = this.getOFDElement("Actions");
        return e == null ? null : new Actions(e);
    }
}
