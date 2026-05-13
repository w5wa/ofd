package org.ofdrw.core.basicStructure.res.resources;


import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.res.OFDResource;
import org.ofdrw.core.compositeObj.CT_VectorG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * contains了一组矢量图像
 * <p>
 * 7.9 Figure 20 Table 18
 *
 * @author Quan Guanyu
 * @since 2019-11-13 07:36:05
 */
public class CompositeGraphicUnits extends OFDElement implements OFDResource {
    public CompositeGraphicUnits(Element proxy) {
        super(proxy);
    }

    public CompositeGraphicUnits() {
        super("CompositeGraphicUnits");
    }


    /**
     * [required]
     * 增加 矢量图像资源描述
     * <p>
     * must have ID attribute
     *
     * @param compositeGraphicUnit 矢量图像资源描述
     * @return this
     */
    public CompositeGraphicUnits addCompositeGraphicUnit(CT_VectorG compositeGraphicUnit) {
        if (compositeGraphicUnit == null) {
            return this;
        }
        if (compositeGraphicUnit.getID() == null) {
            throw new IllegalArgumentException("矢量图像资源描述ID不能为空");
        }
        compositeGraphicUnit.setOFDName("CompositeGraphicUnit");
        this.add(compositeGraphicUnit);
        return this;
    }

    /**
     * [required]
     * 获取 矢量图像资源描述序列
     * <p>
     * must have ID attribute
     *
     * @return 矢量图像资源描述
     */
    public List<CT_VectorG> getCompositeGraphicUnits() {
        return this.getOFDElements("CompositeGraphicUnit",CT_VectorG::new);
    }
}
