package org.ofdrw.core.basicStructure.pageObj.layer.block;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.pageObj.layer.PageBlockType;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.compositeObj.CT_Composite;
import org.ofdrw.core.graph.pathObj.CT_Path;
import org.ofdrw.core.image.CT_Image;

/**
 * 复合对象
 * <p>
 * 见 第 13 章
 * <p>
 * 7.7 Table 16
 *
 * @author Quan Guanyu
 * @since 2019-10-29 17:22:08
 */
public class CompositeObject extends CT_Composite implements PageBlockType {

    public CompositeObject(Element proxy) {
        super(proxy);
    }

    private CompositeObject() {
        super("CompositeObject");
    }

    public CompositeObject(ST_ID id) {
        this();
        this.setObjID(id);
    }


    /**
     * [required attribute]
     * set object ID
     *
     * @param id object ID
     * @return this
     */
    public CompositeObject setID(ST_ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID 不能为空");
        }
        this.setObjID(id);
        return this;
    }

    /**
     * [required attribute]
     * get object ID
     *
     * @return object ID
     */
    public ST_ID getID() {
        return this.getObjID();
    }

    /**
     * [required attribute]
     * 设置 引用resource file中定义的矢量图像标识
     *
     * @param resourceId 引用resource file中定义的矢量图像标识
     * @return this
     */
    public CompositeObject setResourceID(ST_RefID resourceId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("resource file的标识（ResourceID）不能为空");
        }
        this.addAttribute("ResourceID", resourceId.toString());
        return this;
    }

    /**
     * [required attribute]
     * 设置 引用resource file中定义的矢量图像标识
     *
     * @return 引用resource file中定义的矢量图像标识
     */
    public ST_RefID getResourceID() {
        return ST_RefID.getInstance(this.attributeValue("ResourceID"));
    }
}
