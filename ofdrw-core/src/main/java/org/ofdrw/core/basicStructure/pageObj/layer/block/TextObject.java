package org.ofdrw.core.basicStructure.pageObj.layer.block;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.pageObj.layer.PageBlockType;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.text.text.CT_Text;

/**
 * text object
 * <p>
 * 见 11.2
 * <p>
 * 7.7 Table 16
 *
 * @author Quan Guanyu
 * @since 2019-10-29 05:13:54
 */
public class TextObject extends CT_Text implements PageBlockType {
    public TextObject(Element proxy) {
        super(proxy);
    }

    private TextObject() {
        super("TextObject");
    }

    /**
     * @param id object ID
     */
    public TextObject(ST_ID id) {
        this();
        this.setObjID(id);
    }

    /**
     * @param id object ID
     */
    public TextObject(long id) {
        this();
        this.setObjID(new ST_ID(id));
    }


    /**
     * [required attribute]
     * set object ID
     *
     * @param id object ID
     * @return this
     */
    public TextObject setID(ST_ID id) {
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
}
