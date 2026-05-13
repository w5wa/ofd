package org.ofdrw.core.basicStructure.res.resources;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.res.OFDResource;
import org.ofdrw.core.pageDescription.drawParam.CT_DrawParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * contains了一组drawing parameters的描述
 * <p>
 * 7.9 Figure 20 Table 18
 *
 * @author Quan Guanyu
 * @since 2019-11-13 07:36:05
 */
public class DrawParams extends OFDElement implements OFDResource {
    public DrawParams(Element proxy) {
        super(proxy);
    }

    public DrawParams() {
        super("DrawParams");
    }

    /**
     * [required]
     * 增加 drawing parameters描述
     * <p>
     * must have ID attribute
     *
     * @param drawParam drawing parameters描述
     * @return this
     */
    public DrawParams addDrawParam(CT_DrawParam drawParam) {
        if (drawParam == null) {
            return this;
        }
        if (drawParam.getID() == null) {
            throw new IllegalArgumentException("drawing parameters描述ID不能为空");
        }
        this.add(drawParam);
        return this;
    }

    /**
     * [required]
     * 获取 drawing parameters描述序列
     *
     * @return drawing parameters描述
     */
    public List<CT_DrawParam> getDrawParams() {
        return this.getOFDElements("DrawParam", CT_DrawParam::new);
    }
}
