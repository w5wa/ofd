package org.ofdrw.core.basicStructure.res;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * 多媒体
 * <p>
 * 7.9 Resources - Figure 21 Table 19
 *
 * @author Quan Guanyu
 * @since 2019-11-13 08:03:34
 */
public class CT_MultiMedia extends OFDElement {
    public CT_MultiMedia(Element proxy) {
        super(proxy);
    }

    public CT_MultiMedia() {
        super("MultiMedia");
    }


    public ST_ID getID() {
        return this.getObjID();
    }

    public CT_MultiMedia setID(ST_ID id) {
        this.setObjID(id);
        return this;
    }

    /**
     * [required attribute]
     * 设置 多媒体类型
     * <p>
     * supports bitmap images, video, and audio
     *
     * @param type 多媒体类型
     * @return this
     */
    public CT_MultiMedia setType(MediaType type) {
        if (type == null) {
            throw new IllegalArgumentException("多媒体类型（Type）为空");
        }
        this.addAttribute("Type", type.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 多媒体类型
     * <p>
     * supports bitmap images, video, and audio
     *
     * @return 多媒体类型
     */
    public MediaType getType() {
        return MediaType.getInstance(this.attributeValue("Type"));
    }

    /**
     * [optional attribute]
     * 设置 资源的格式
     * <p>
     * supports BMP, JPEG, PNG, TIFF, and AVS formats; TIFF does not support multi-page
     *
     * @param format 资源的格式
     * @return this
     */
    public CT_MultiMedia setFormat(String format) {
        if (format == null) {
            this.removeAttr("Format");
            return this;
        }
        this.addAttribute("Format", format);
        return this;
    }

    /**
     * [optional attribute]
     * 获取 资源的格式
     * <p>
     * supports BMP, JPEG, PNG, TIFF, and AVS formats; TIFF does not support multi-page
     *
     * @return 资源的格式
     */
    public String getFormat() {
        return this.attributeValue("Format");
    }


    /**
     * [required]
     * 设置 points to within the OFD package的多媒体文件位置
     *
     * @param mediaFile points to within the OFD package的多媒体文件位置
     * @return this
     */
    public CT_MultiMedia setMediaFile(ST_Loc mediaFile) {
        if (mediaFile == null) {
            throw new IllegalArgumentException("");
        }
        this.removeAll();
        this.addOFDEntity("MediaFile", mediaFile);
        return this;
    }

    /**
     * [required]
     * 获取 points to within the OFD package的多媒体文件位置
     *
     * @return points to within the OFD package的多媒体文件位置
     */
    public ST_Loc getMediaFile() {
        Element e = this.getOFDElement("MediaFile");
        return e == null ? null : new ST_Loc(e.getTextTrim());
    }
}
