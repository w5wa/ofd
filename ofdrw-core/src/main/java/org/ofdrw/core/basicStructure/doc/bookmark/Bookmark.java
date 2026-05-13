package org.ofdrw.core.basicStructure.doc.bookmark;

import org.dom4j.Element;
import org.ofdrw.core.action.actionType.actionGoto.CT_Dest;
import org.ofdrw.core.OFDElement;

/**
 * 本标准支持书签，可以将常用位置定义为书签，
 * 文档可以contains一组书签。
 * <p>
 * 7.5 图 11 书签结构
 *
 * @author Quan Guanyu
 * @since 2019-10-09 08:06:35
 */
public class Bookmark extends OFDElement {
    public Bookmark(Element proxy) {
        super(proxy);
    }

    public Bookmark() {
        super("Bookmark");
    }

    /**
     * @param name bookmark name
     * @param dest document page position for this bookmark
     */
    public Bookmark(String name, CT_Dest dest) {
        this();
        this.setBookmarkName(name)
                .setDest(dest);
    }

    /**
     * [required attribute]
     * 设置 bookmark name
     *
     * @param name bookmark name
     * @return this
     */
    public Bookmark setBookmarkName(String name) {
        this.addAttribute("Name", name);
        return this;
    }

    /**
     * [required attribute]
     * 获取 bookmark name
     *
     * @return bookmark name
     */
    public String getBookmarkName() {
        return this.attributeValue("Name");
    }

    /**
     * [required]
     * 设置 document page position for this bookmark
     * <p>
     * see Table 54
     *
     * @param dest document page position for this bookmark
     * @return this
     */
    public Bookmark setDest(CT_Dest dest) {
        this.set(dest);
        return this;
    }

    /**
     * [required]
     * 获取 document page position for this bookmark
     * <p>
     * see Table 54
     *
     * @return document page position for this bookmark
     */
    public CT_Dest getDest() {
        Element e = this.getOFDElement("Dest");
        return e == null ? null : new CT_Dest(e);
    }
}
