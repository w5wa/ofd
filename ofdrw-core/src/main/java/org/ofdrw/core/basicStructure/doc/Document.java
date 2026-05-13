package org.ofdrw.core.basicStructure.doc;

import org.dom4j.Element;
import org.ofdrw.core.action.Actions;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.doc.bookmark.Bookmarks;
import org.ofdrw.core.basicStructure.doc.permission.CT_Permission;
import org.ofdrw.core.basicStructure.doc.vpreferences.CT_VPreferences;
import org.ofdrw.core.basicStructure.outlines.Outlines;
import org.ofdrw.core.basicStructure.pageTree.Pages;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * 文档root node
 * Document.xml
 * <p>
 * ————《GB/T 33190-2016》 图 5
 *
 * @author Quan Guanyu
 * @since 2019-10-04 07:43:14
 */
public class Document extends OFDElement {
    public Document(Element proxy) {
        super(proxy);
    }

    public Document() {
        super("Document");
    }

    /**
     * [required]
     * 设置 文档公共数据
     * <p>
     * defines page area and public resources
     *
     * @param commonData 文档公共数据
     * @return this
     */
    public Document setCommonData(CT_CommonData commonData) {
        this.set(commonData);
        return this;
    }

    /**
     * [required]
     * 获取 文档公共数据
     * <p>
     * defines page area and public resources
     *
     * @return 文档公共数据
     */
    public CT_CommonData getCommonData() {
        Element e = this.getOFDElement("CommonData");
        return e == null ? null : new CT_CommonData(e);
    }

    /**
     * [required]
     * 设置 页树
     * <p>
     * see section 7.6 for page tree description keys
     *
     * @param pages 页树
     * @return this
     */
    public Document setPages(Pages pages) {
        this.set(pages);
        return this;
    }

    /**
     * [required]
     * 获取 页树
     * <p>
     * see section 7.6 for page tree description keys
     *
     * @return 页树
     */
    public Pages getPages() {
        Element e = this.getOFDElement("Pages");
        return e == null ? null : new Pages(e);
    }

    /**
     * [optional]
     * 设置 大纲
     *
     * @param outlines 大纲
     * @return this
     */
    public Document setOutlines(Outlines outlines) {
        this.set(outlines);
        return this;
    }

    /**
     * [optional]
     * 获取 大纲
     *
     * @return 大纲
     */
    public Outlines getOutlines() {
        Element e = this.getOFDElement("Outlines");
        return e == null ? null : new Outlines(e);
    }

    /**
     * [optional]
     * 设置 文档的权限声明
     *
     * @param permission 文档的权限声明
     * @return this
     */
    public Document setPermissions(CT_Permission permission) {
        this.set(permission);
        return this;
    }

    /**
     * [optional]
     * 获取 文档的权限声明
     *
     * @return 文档的权限声明
     */
    public CT_Permission getPermission() {
        Element e = this.getOFDElement("Permissions");
        return e == null ? null : new CT_Permission(e);
    }

    /**
     * [optional]
     * 设置 文档关联的action sequence
     * <p>
     * when multiple Action objects exist, all actions are executed in order
     *
     * @param actions 文档关联的action sequence
     * @return this
     */
    public Document setActions(Actions actions) {
        this.set(actions);
        return this;
    }

    /**
     * [optional]
     * 获取 文档关联的action sequence
     * <p>
     * when multiple Action objects exist, all actions are executed in order
     *
     * @return 文档关联的action sequence
     */
    public Actions getActions() {
        Element e = this.getOFDElement("Actions");
        return e == null ? null : new Actions(e);
    }

    /**
     * [optional]
     * 设置 文档的视图首选项
     *
     * @param vPreferences 文档的视图首选项
     * @return this
     */
    public Document setVPreferences(CT_VPreferences vPreferences) {
        this.set(vPreferences);
        return this;
    }

    /**
     * [optional]
     * 获取 文档的视图首选项
     *
     * @return 文档的视图首选项
     */
    public CT_VPreferences getVPreferences() {
        Element e = this.getOFDElement("VPreferences");
        return e == null ? null : new CT_VPreferences(e);
    }


    /**
     * [optional]
     * 设置 文档的书签集，contains一组书签
     * <p>
     * 7.5 Document Root Node - Table 5 Document Root Node Attributes
     *
     * @param bookmarks 文档的书签集
     * @return this
     */
    public Document setBookmarks(Bookmarks bookmarks) {
        this.set(bookmarks);
        return this;
    }

    /**
     * [optional]
     * 获取 文档的书签集，contains一组书签
     * <p>
     * 7.5 Document Root Node - Table 5 Document Root Node Attributes
     *
     * @return 文档的书签集
     */
    public Bookmarks getBookmarks() {
        Element e = this.getOFDElement("Bookmarks");
        return e == null ? null : new Bookmarks(e);
    }

    /**
     * [optional]
     * 设置 points to注释列表的文件
     * <p>
     * see chapter 15 for annotation descriptions
     *
     * @param annotations points to注释列表的file path
     * @return this
     */
    public Document setAnnotations(ST_Loc annotations) {
        this.setOFDEntity("Annotations", annotations.toString());
        return this;
    }

    /**
     * [optional]
     * 获取 points to注释列表的文件
     * <p>
     * see chapter 15 for annotation descriptions
     *
     * @return points to注释列表的file path
     */
    public ST_Loc getAnnotations() {
        return ST_Loc.getInstance(this.getOFDElementText("Annotations"));
    }

    /**
     * [optional]
     * 设置 points to自定义标引列表文件
     * <p>
     * see chapter 16 for custom index descriptions
     *
     * @param customTags points to自定义标引列表file path
     * @return this
     */
    public Document setCustomTags(ST_Loc customTags) {
        this.setOFDEntity("CustomTags", customTags.toString());
        return this;
    }

    /**
     * [optional]
     * 获取 points to自定义标引列表文件
     * <p>
     * see chapter 16 for custom index descriptions
     *
     * @return points to自定义标引列表file path
     */
    public ST_Loc getCustomTags() {
        return ST_Loc.getInstance(this.getOFDElementText("CustomTags"));
    }

    /**
     * [optional]
     * 设置 points to附件列表文件
     * <p>
     * see chapter 20 for attachment descriptions
     *
     * @param attachments points to附件列表file path
     * @return this
     */
    public Document setAttachments(ST_Loc attachments) {
        this.setOFDEntity("Attachments", attachments.toString());
        return this;
    }

    /**
     * [optional]
     * 获取 points to附件列表文件
     * <p>
     * see chapter 20 for attachment descriptions
     *
     * @return points to附件列表file path
     */
    public ST_Loc getAttachments() {
        return ST_Loc.getInstance(this.getOFDElementText("Attachments"));
    }

    /**
     * [optional]
     * set reference to extension list file
     * <p>
     * see chapter 17 for extension list file
     *
     * @param extensions points to扩展列表file path
     * @return this
     */
    public Document setExtensions(ST_Loc extensions) {
        this.setOFDEntity("Extensions", extensions.toString());
        return this;
    }

    /**
     * [optional]
     * set reference to extension list file
     * <p>
     * see chapter 17 for extension list file
     *
     * @return points to扩展列表file path
     */
    public ST_Loc getExtensions() {
        return ST_Loc.getInstance(this.getOFDElementText("Extensions"));
    }

}
