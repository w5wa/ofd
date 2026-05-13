package org.ofdrw.core.compositeObj;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.pageObj.layer.PageBlockType;
import org.ofdrw.core.basicStructure.pageObj.layer.block.CT_PageBlock;
import org.ofdrw.core.basicType.STBase;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_RefID;

import java.util.List;

/**
 * 矢量图像
 * <p>
 * when referencing resources from composite objects: vector images in Res (CompositeGraphUnit)
 * <p>
 * 13 图 72 表 50
 *
 * @author Quan Guanyu
 * @since 2019-10-27 04:56:42
 */
public class CT_VectorG extends OFDElement {

    public CT_VectorG(Element proxy) {
        super(proxy);
    }

    public CT_VectorG() {
        super("VectorG");
    }

    protected CT_VectorG(String name) {
        super(name);
    }

    public ST_ID getID() {
        return this.getObjID();
    }

    public CT_VectorG setID(ST_ID id) {
        this.setObjID(id);
        return this;
    }

    /**
     * [required attribute]
     * 设置 矢量图像的width
     * <p>
     * clip parts that exceed the area
     *
     * @param width 矢量图像的width
     * @return this
     */
    public CT_VectorG setWidth(Double width) {
        if (width == null) {
            throw new IllegalArgumentException("矢量图像的width（Width）不能为空");
        }
        this.addAttribute("Width", STBase.fmt(width));
        return this;
    }

    /**
     * [required attribute]
     * 获取 矢量图像的width
     * <p>
     * clip parts that exceed the area
     *
     * @return 矢量图像的width
     */
    public Double getWidth() {
        String str = this.attributeValue("Width");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("格式非法无法获取到矢量图像的width（Width）");
        }
        return Double.parseDouble(str);
    }

    /**
     * [required attribute]
     * 设置 矢量图像的height
     * <p>
     * clip parts that exceed the area
     *
     * @param height 矢量图像的height
     * @return this
     */
    public CT_VectorG setHeight(Double height) {
        if (height == null) {
            throw new IllegalArgumentException("矢量图像的height（Height）不能为空");
        }
        this.addAttribute("Height", STBase.fmt(height));
        return this;
    }

    /**
     * [required attribute]
     * 获取 矢量图像的height
     * <p>
     * clip parts that exceed the area
     *
     * @return 矢量图像的height
     */
    public Double getHeight() {
        String str = this.attributeValue("Height");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("格式非法无法获取到矢量图像的width（Height）");
        }
        return Double.parseDouble(str);
    }

    /**
     * [optional]
     * 设置 缩略图
     * <p>
     * points to image file within package
     *
     * @param thumbnail 缩略图路径
     * @return this
     */
    public CT_VectorG setThumbnail(ST_RefID thumbnail) {
        this.setOFDEntity("Thumbnail", thumbnail.toString());
        return this;
    }

    /**
     * [optional]
     * 获取 缩略图
     * <p>
     * points to image file within package
     *
     * @return 缩略图路径
     */
    public ST_RefID getThumbnail() {
        return ST_RefID.getInstance(this.getOFDElementText("Thumbnail"));
    }

    /**
     * [optional]
     * 设置 替换图像
     * <p>
     * 用于高分辨率输出时将缩略图替换为此高分辨率的图像
     * points to image file within package
     *
     * @param substitution 替换图像
     * @return this
     */
    public CT_VectorG setSubstitution(ST_RefID substitution) {
        this.setOFDEntity("Substitution", substitution.toString());
        return this;
    }

    /**
     * [optional]
     * 获取 替换图像
     * <p>
     * 用于高分辨率输出时将缩略图替换为此高分辨率的图像
     * points to image file within package
     *
     * @return 替换图像
     */
    public ST_RefID getSubstitution() {
        return ST_RefID.getInstance(this.getOFDElementText("Substitution"));
    }


    /**
     * [required]
     * 设置 内容的矢量描述
     *
     * @param content 内容的矢量描述
     * @return this
     */
    public CT_VectorG setContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("内容的矢量描述（Content）不能为空");
        }
        this.set(content);
        return this;
    }

    /**
     * [required]
     * 增加 内容的矢量描述
     *
     * @param blockType 内容的矢量描述
     * @return this
     */
    public CT_VectorG addContent(PageBlockType blockType) {
        if (blockType == null) {
            return this;
        }
        Element e = this.getOFDElement("Content");
        Content content = (e == null) ? new Content() : new Content(e);
        content.addPageBlock(blockType);
        this.set(content);
        return this;
    }

    /**
     * [required]
     * 获取 内容的矢量描述
     *
     * @return 内容的矢量描述
     */
    public Content getContent() {
        Element e = this.getOFDElement("Content");
        if (e == null) {
            throw new IllegalArgumentException("没有找到Content元素");
        }
        return new Content(e);
    }
}
