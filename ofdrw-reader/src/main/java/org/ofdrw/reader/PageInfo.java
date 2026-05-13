package org.ofdrw.reader;

import org.ofdrw.core.basicStructure.pageObj.Page;
import org.ofdrw.core.basicStructure.pageObj.layer.CT_Layer;
import org.ofdrw.core.basicStructure.pageObj.layer.Type;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.reader.model.TemplatePageEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * page information
 *
 * @author Quan Guanyu
 * @since 2020-05-13 19:10:02
 */
public class PageInfo {

    /**
     * 页面的物理大小
     */
    private ST_Box size;
    /**
     * 页面底层对象
     */
    private Page obj;
    /**
     * 页面在OFD中的object ID
     */
    private ST_ID id;
    /**
     * page number，从1起
     */
    private Integer index;

    /**
     * 该页面引用的模板页面
     */
    private ArrayList<TemplatePageEntity> templates = new ArrayList<>();

    /**
     * 页面的absolute path
     */
    private ST_Loc pageAbsLoc;

    /**
     * page number目录文件的序号
     */
    private Integer pageN;

    public PageInfo() {
    }

    public ST_Box getSize() {
        return size;
    }

    public PageInfo setSize(ST_Box size) {
        this.size = size;
        return this;
    }

    public Page getObj() {
        return obj;
    }

    public PageInfo setObj(Page obj) {
        this.obj = obj;
        return this;
    }

    public ST_ID getId() {
        return id;
    }

    public PageInfo setId(ST_ID id) {
        this.id = id;
        return this;
    }

    public Integer getIndex() {
        return index;
    }

    public PageInfo setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public ST_Loc getPageAbsLoc() {
        return pageAbsLoc;
    }

    /**
     * 设置页面的absolute path
     * <p>
     * 同时设置 页面的索引号 Page_N
     *
     * @param pageAbsLoc absolute path
     * @return this
     */
    public PageInfo setPageAbsLoc(ST_Loc pageAbsLoc) {
        this.pageAbsLoc = pageAbsLoc;
        return this;
    }

    /**
     * 获取 Page_N容器 N的number
     * @return N的number
     */
    public Integer getPageN() {
        return pageN;
    }

    /**
     * 设置 Page_N容器 N的number
     * @param pageN number
     * @return this
     */
    public PageInfo setPageN(Integer pageN) {
        this.pageN = pageN;
        return this;
    }

    /**
     * 获取按照order和出现顺序页面和模板内容
     *
     * @return 页面和模板内容
     */
    public List<Page> getOrderRelatedPageList() {
        ArrayList<TemplatePageEntity> res = new ArrayList<>(templates);
        res.add(new TemplatePageEntity(Type.Body, obj));
        // 按照order对数组进行排序
        res.sort(Comparator.comparingInt(p -> p.getZOrder().order()));
        return res.stream().map(TemplatePageEntity::getPage).collect(Collectors.toList());
    }

    /**
     * 获取整个页面的layer列表（contains模板）
     *
     * @return 页面所有layer
     */
    public List<CT_Layer> getAllLayer() {
        List<CT_Layer> layerList = new ArrayList<>();
        // 获取排好序的页面列表（contains页面模板和页面本身）
        for (Page page : getOrderRelatedPageList()) {
            if (page.getContent() != null) {
                layerList.addAll(page.getContent().getOrderedLayers());
            }
        }
        return layerList;
    }

    public PageInfo setTemplates(ArrayList<TemplatePageEntity> templates) {
        this.templates = templates;
        return this;
    }
}
