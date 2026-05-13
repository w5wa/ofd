package org.ofdrw.core.basicStructure.pageTree;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 页树
 * <p>
 * 图 12 页树结构
 *
 * @author Quan Guanyu
 * @since 2019-10-05 10:39:31
 */
public class Pages extends OFDElement {
    public Pages(Element proxy) {
        super(proxy);
    }

    public Pages() {
        super("Pages");
    }


    /**
     * [required]
     * 增加 叶节点
     * <p>
     * a page tree can contain one or more leaf nodes; page order is
     * determined by the order of leaf nodes in a pre-order traversal of the page tree.
     *
     * @param page 叶节点
     * @return this
     */
    public Pages addPage(Page page) {
        this.add(page);
        return this;
    }

    /**
     * 获取页面数量
     *
     * @return 页面数量
     */
    public int getSize() {
        return this.elements().size();
    }


    /**
     * [required]
     * 获取 叶节点序列
     * <p>
     * a page tree can contain one or more leaf nodes; page order is
     * determined by the order of leaf nodes in a pre-order traversal of the page tree.
     *
     * @return 叶节点序列 （大于等于 1）
     */
    public List<Page> getPages() {
        return this.getOFDElements("Page",Page::new);
    }

    /**
     * 获取指定页面
     * @param index page index（page number - 1）
     * @return 页节点
     */
    public Page getPageByIndex(int index) {
        return getPages().get(index);
    }
}
