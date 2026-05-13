package org.ofdrw.core.image;

import org.dom4j.Element;
import org.ofdrw.core.basicStructure.pageObj.layer.block.ImageObject;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.pageDescription.CT_GraphicUnit;

/**
 * 图像
 * <p>
 * 10 图像 图 57 表 43
 *
 * @author Quan Guanyu
 * @since 2019-10-22 06:32:35
 */
public class CT_Image extends CT_GraphicUnit<CT_Image> {
    public CT_Image(Element proxy) {
        super(proxy);
    }

    public CT_Image() {
        super("Image");
    }

    protected CT_Image(String name) {
        super(name);
    }

    /**
     * [required attribute]
     * 设置 引用resource file的定义多媒体的标识
     *
     * @param resourceId 引用resource file的定义多媒体的标识
     * @return this
     */
    public CT_Image setResourceID(ST_RefID resourceId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("resource file的标识（ResourceID）不能为空");
        }
        this.addAttribute("ResourceID", resourceId.toString());
        return this;
    }

    /**
     * [required attribute]
     * 设置 引用resource file的定义多媒体的标识
     *
     * @return 引用resource file的定义多媒体的标识
     */
    public ST_RefID getResourceID() {
        return ST_RefID.getInstance(this.attributeValue("ResourceID"));
    }

    /**
     * [optional attribute]
     * 设置 可替换图像
     * <p>
     * 引用resource file中定义的多媒体的标识，由于某些情况
     * 如高分辨率输出进行图像替换
     *
     * @param substitution 可替换图像标识
     * @return this
     */
    public CT_Image setSubstitution(ST_RefID substitution) {
        if (substitution == null) {
            this.removeAttr("Substitution");
            return this;
        }
        this.addAttribute("Substitution", substitution.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 可替换图像引用
     * <p>
     * 引用resource file中定义的多媒体的标识，由于某些情况
     * 如高分辨率输出进行图像替换
     *
     * @return 可替换图像标识引用
     */
    public ST_RefID getSubstitution() {
        return ST_RefID.getInstance(this.attributeValue("Substitution"));
    }

    /**
     * [optional attribute]
     * 设置 图像蒙版
     * <p>
     * 引用resource file中定义的多媒体的标识，用作蒙板的图像应是
     * 与 ResourceID points to的图像相同大小的二值图
     *
     * @param imageMask 图像蒙版资源引用
     * @return this
     */
    public CT_Image setImageMask(ST_RefID imageMask) {
        if (imageMask == null) {
            this.removeAttr("ImageMask");
            return this;
        }
        this.addAttribute("ImageMask", imageMask.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取 图像蒙版资源引用
     * <p>
     * 引用resource file中定义的多媒体的标识，用作蒙板的图像应是
     * 与 ResourceID points to的图像相同大小的二值图
     *
     * @return 图像蒙版资源引用
     */
    public ST_RefID getImageMask() {
        return ST_RefID.getInstance(this.attributeValue("ImageMask"));
    }


    /**
     * [optional]
     * 设置 图像边框
     *
     * @param border 图像边框
     * @return this
     */
    public CT_Image setBorder(Border border) {
        if (border == null) {
            return this;
        }
        this.set(border);
        return this;
    }

    /**
     * 构造image对象
     *
     * @param id object ID
     * @return object
     */
    public ImageObject toObj(ST_ID id) {
        this.setOFDName("ImageObject");
        this.setObjID(id);
        return new ImageObject(this);
    }

    /**
     * [optional]
     * 设置 图像边框
     *
     * @return 图像边框
     */
    public Border getBorder() {
        Element e = this.getOFDElement("Border");
        return e == null ? null : new Border(e);
    }
}
