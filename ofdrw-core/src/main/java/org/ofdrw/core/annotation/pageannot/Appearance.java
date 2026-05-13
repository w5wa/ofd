package org.ofdrw.core.annotation.pageannot;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.pageObj.layer.PageBlockType;
import org.ofdrw.core.basicStructure.pageObj.layer.block.CT_PageBlock;
import org.ofdrw.core.basicType.ST_Box;

/**
 * 注释的静态呈现效果
 * <p>
 * described using page block definitions
 * <p>
 * 15.2 Figure 81 Table 61
 *
 * @author Quan Guanyu
 * @since 2019-11-19 05:48:28
 */
public class Appearance extends CT_PageBlock {

    public Appearance(Element proxy) {
        super(proxy);
    }

    public Appearance(ST_Box boundary) {
        super("Appearance");
        setBoundary(boundary);
    }

    /**
     * [required]
     * 设置 边界
     * <p>
     * 附录 A.4
     *
     * @param boundary 边界
     * @return this
     */
    public Appearance setBoundary(ST_Box boundary) {
        if (boundary == null) {
            throw new IllegalArgumentException("Boundary 不能为空");
        }
        this.addAttribute("Boundary", boundary.toString());
        return this;
    }

    /**
     * [required]
     *
     * @return 边界
     */
    public ST_Box getBoundary() {
        return ST_Box.getInstance(this.attributeValue("Boundary"));
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
    @Override
    public Appearance addPageBlock(PageBlockType pageBlock) {
        super.addPageBlock(pageBlock);
        return this;
    }
}
