package org.ofdrw.core.basicStructure.pageObj.layer.block;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.pageObj.layer.PageBlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 页块结构
 * <p>
 * 可以嵌套
 * <p>
 * 7.7 页对象 图 17 表 16
 *
 * @author Quan Guanyu
 * @since 2019-10-10 06:39:58
 */
public class CT_PageBlock extends OFDElement implements PageBlockType {
    public CT_PageBlock(Element proxy) {
        super(proxy);
    }

    protected CT_PageBlock(String name) {
        super(name);
    }

    public CT_PageBlock() {
        this("PageBlock");
    }


    /**
     * [optional]
     * add page block
     * <p>
     * a page block can nest other page blocks and contain 0 or more page blocks
     *
     * @param pageBlock page block instance
     * @return this
     */
    public CT_PageBlock addPageBlock(PageBlockType pageBlock) {
        this.add(pageBlock);
        return this;
    }

    /**
     * [optional]
     * 获取 当前页块内的所有页块
     * <p>
     * a page block can nest other page blocks and contain 0 or more page blocks
     * <p>
     * Tip： 从列表取出的元素可以使用<code>instanceof</code> 判断元素的类型
     *
     * @return 当前页块内的所有页块
     */
    public List<PageBlockType> getPageBlocks() {
        List<Element> elements = this.elements();
        List<PageBlockType> res = new ArrayList<>(elements.size());
        for (Element element : elements) {
            PageBlockType pb = PageBlockType.getInstance(element);
            if (pb != null){
                res.add(pb);
            }
        }
        return res;
    }


}
