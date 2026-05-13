package org.ofdrw.core.basicStructure.pageObj.layer.block;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.pageObj.layer.PageBlockType;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.graph.pathObj.CT_Path;

/**
 * graphics object
 * <p>
 * 见 9.1
 * <p>
 * 7.7 Table 16
 *
 * @author Quan Guanyu
 * @since 2019-10-29 05:14:52
 */
public class PathObject extends CT_Path implements PageBlockType {

    public PathObject(Element proxy) {
        super(proxy);
    }

    private PathObject() {
        super("PathObject");
    }

    public PathObject(ST_ID id){
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
    public PathObject setID(ST_ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID 不能为空");
        }
        this.setObjID(id);
        return this;
    }

    public PathObject setID(long id) {
        return setID(new ST_ID(id));
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
}
