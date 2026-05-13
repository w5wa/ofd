package org.ofdrw.core.basicStructure.outlines;

import org.dom4j.Element;
import org.ofdrw.core.action.Actions;
import org.ofdrw.core.OFDElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * outline node
 * <p>
 * Figure 19: Outline Node Structure
 *
 * @author Quan Guanyu
 * @since 2019-10-05 11:13:47
 */
public class CT_OutlineElem extends OFDElement {
    public CT_OutlineElem(Element proxy) {
        super(proxy);
    }

    public CT_OutlineElem() {
        super("OutlineElem");
    }

    public CT_OutlineElem(String title) {
        this();
        this.setTitle(title);
    }

    /**
     * [required attribute]
     * 设置 outline node标题
     *
     * @param title outline node标题
     * @return this
     */
    public CT_OutlineElem setTitle(String title) {
        this.addAttribute("Title", title);
        return this;
    }

    /**
     * [required attribute]
     * 获取 outline node标题
     *
     * @return outline node标题
     */
    public String getTitle() {
        return this.attributeValue("Title");
    }

    /**
     * [optional attribute]
     * 设置 该节点下所有叶节点的数目参考值
     * <p>
     * should be based on the actual number of child nodes under this node
     * <p>
     * default value: 0
     *
     * @param count 该节点下所有叶节点的数目参考值
     * @return this
     */
    public CT_OutlineElem setCount(int count) {
        this.addAttribute("Count", Integer.toString(count));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 该节点下所有叶节点的数目参考值
     * <p>
     * should be based on the actual number of child nodes under this node
     * <p>
     * default value: 0
     *
     * @return 该节点下所有叶节点的数目参考值
     */
    public Integer getCount() {
        String str = this.attributeValue("Count");
        if (str == null || str.trim().length() == 0) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    /**
     * [optional attribute]
     * valid when child nodes exist; if true,
     * the outline expands child nodes in its initial state;
     * if false, child nodes are not expanded
     * <p>
     * default value: true
     *
     * @param expanded true - 展开； false - 不展开
     * @return this
     */
    public CT_OutlineElem setExpanded(boolean expanded) {
        this.addAttribute("Expanded", Boolean.toString(expanded));
        return this;
    }

    /**
     * [optional attribute]
     * valid when child nodes exist; if true,
     * the outline expands child nodes in its initial state;
     * if false, child nodes are not expanded
     * <p>
     * default value: true
     *
     * @return true - 展开； false - 不展开
     */
    public Boolean getExpanded() {
        String str = this.attributeValue("Expanded");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional]
     * 设置 当此outline node被激活时执行的action sequence
     *
     * @param actions action sequence
     * @return this
     */
    public CT_OutlineElem setActions(Actions actions) {
        this.set(actions);
        return this;
    }

    /**
     * [optional]
     * 获取 当此outline node被激活时执行的action sequence
     *
     * @return action sequence，可能为null
     */
    public Actions getActions() {
        Element e = this.getOFDElement("Actions");
        return e == null ? null : new Actions(e);
    }

    /**
     * [optional]
     * 增加 大纲子节点
     * <p>
     * child outline nodes of this node; nested to form a tree structure
     *
     * @param outlineElem 大纲子节点
     * @return this
     */
    public CT_OutlineElem addOutlineElem(CT_OutlineElem outlineElem) {
        this.add(outlineElem);
        return this;
    }

    /**
     * [optional]
     * 获取 该大纲下所有子节点
     * <p>
     * child outline nodes of this node; nested to form a tree structure
     *
     * @return 该大纲下所有子节点
     */
    public List<CT_OutlineElem> getOutlineElems() {
        return this.getOFDElements("OutlineElem",CT_OutlineElem::new);
    }
}
