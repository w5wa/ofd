package org.ofdrw.layout.areaholder;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

import java.util.List;

/**
 * 【OFDRW 扩展】 area placeholder block list AreaHolderBlocks.xml
 * <p>
 * area placeholder block list 是 OFDRW 扩展的对象，area placeholder block list可以contains文档中的多个 区域占位区块对象 ，注意每个 区域占位区块对象 的
 * area name在 area placeholder block list 中应该唯一。
 * <p>
 * 通常来说 AreaHolderBlocks.xml 位于document directory中，例如：`/Doc_0/AreaHolderBlocks.xml`。
 *
 * @author Quan Guanyu
 * @since 2023-10-28 15:30:36
 */
public class AreaHolderBlocks extends OFDElement {

    /**
     * 【OFDRW 扩展】 area placeholder block list file name
     */
    public static final String AreaHolderBlocksFile = "AreaHolderBlocks.xml";

    public AreaHolderBlocks(Element proxy) {
        super(proxy);
    }

    public AreaHolderBlocks() {
        super("AreaHolderBlocks");
    }


    /**
     * [optional]
     * 增加 区域占位区块
     *
     * @param area 区域占位区块
     * @return this
     */
    public AreaHolderBlocks addAreaHolderBlock(CT_AreaHolderBlock ...area) {
        if (area == null || area.length == 0) {
            return this;
        }
        for (CT_AreaHolderBlock holderBlock : area) {
            this.add(holderBlock);
        }
        return this;
    }

    /**
     * [optional]
     * 获取 区域占位区块 序列
     *
     * @return 区域占位区块 序列
     */
    public List<CT_AreaHolderBlock> getAreaHolderBlocks() {
        return this.getOFDElements("AreaHolderBlock", CT_AreaHolderBlock::new);
    }
}
