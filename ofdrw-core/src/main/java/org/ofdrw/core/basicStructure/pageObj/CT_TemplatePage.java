package org.ofdrw.core.basicStructure.pageObj;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.pageObj.layer.Type;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * 模板页
 * <p>
 * ————《GB/T 33190-2016》 图 14
 *
 * @author Quan Guanyu
 * @since 2019-10-04 10:37:10
 */
public class CT_TemplatePage extends OFDElement {
    public CT_TemplatePage(Element proxy) {
        super(proxy);
    }

    public CT_TemplatePage() {
        super("TemplatePage");
    }

    /**
     * [required attribute]
     * 设置 模板页的标识符，不能与已有标识符重复
     *
     * @param id 模板页的标识符
     * @return this
     */
    public CT_TemplatePage setID(ST_ID id) {
        this.addAttribute("ID", id.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 模板页的标识符，不能与已有标识符重复
     *
     * @return 模板页的标识符
     */
    public ST_ID getID() {
        return ST_ID.getInstance(this.attributeValue("ID"));
    }


    /**
     * [optional attribute]
     * 设置 模板页名称
     *
     * @param name 模板页名称
     * @return this
     */
    public CT_TemplatePage setTemplatePageName(String name) {
        if (name == null || name.trim().length() == 0) {
            this.removeAttr("Name");
            return this;
        }

        this.addAttribute("Name", name);
        return this;
    }

    /**
     * [optional attribute]
     * get template page name
     *
     * @return 模板页名称
     */
    public ST_ID getTemplatePageName() {
        return ST_ID.getInstance(this.attributeValue("Name"));
    }


    /**
     * [optional attribute]
     * 设置 default view type of template page
     * <p>
     * its type description and rendering order are consistent with the Type in Layer; see Table 15
     * if multiple referenced templates have the same order attribute, display by reference order
     * referenced first is drawn first
     * <p>
     * default value: Background
     *
     * @param zOrder default view type of template page
     * @return this
     */
    public CT_TemplatePage setZOrder(Type zOrder) {
        if (zOrder == null) {
            this.removeAttr("ZOrder");
            return this;
        }
        this.addAttribute("ZOrder", zOrder.toString());
        return this;
    }

    /**
     * [optional attribute]
     * get default view type of template page
     * <p>
     * its type description and rendering order are consistent with the Type in Layer; see Table 15
     * if multiple referenced templates have the same order attribute, display by reference order
     * referenced first is drawn first
     * <p>
     * default value: Background
     *
     * @return default view type of template page
     */
    public Type getZOrder() {
        String value = this.attributeValue("ZOrder");
        return (value == null || value.trim().length() == 0) ? Type.Background : Type.getInstance(value);
    }


    /**
     * [optional attribute]
     * 设置 points to模板页内容描述文件 路径
     *
     * @param baseLoc points to模板页内容描述文件 路径
     * @return this
     */
    public CT_TemplatePage setBaseLoc(ST_Loc baseLoc) {
        if (baseLoc == null) {
            this.removeAttr("BaseLoc");
            return this;
        }
        this.addAttribute("BaseLoc", baseLoc.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 points to模板页内容描述文件 路径
     *
     * @return points to模板页内容描述文件 路径
     */
    public ST_Loc getBaseLoc() {
        return ST_Loc.getInstance(this.attributeValue("BaseLoc"));
    }
}
