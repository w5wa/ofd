package org.ofdrw.core.basicStructure.doc;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Box;

/**
 * page area结构
 * <p>
 * ————《GB/T 33190-2016》 图 7
 *
 * @author Quan Guanyu
 * @since 2019-10-04 08:58:55
 */
public class CT_PageArea extends OFDElement {
    public CT_PageArea(Element proxy) {
        super(proxy);
    }

    public CT_PageArea() {
        super("PageArea");
    }

    /**
     * physical page area 创建区域
     *
     * @param topLeftX physical page area X coordinate of top-left corner
     * @param topLeftY physical page area Y coordinate of top-left corner
     * @param width    physical page area width
     * @param height   physical page area height
     */
    public CT_PageArea(double topLeftX,
                       double topLeftY,
                       double width,
                       double height) {
        this();
        this.setPhysicalBox(topLeftX, topLeftY, width, height);
    }

    /**
     * [required]
     * set physical page area
     * <p>
     * the top-left corner is the origin of the page coordinate system
     *
     * @param physicalBox physical page area
     * @return this
     */
    public CT_PageArea setPhysicalBox(ST_Box physicalBox) {
        this.setOFDEntity("PhysicalBox", physicalBox.toString());
        return this;
    }

    /**
     * [required]
     * set physical page area
     * <p>
     * the top-left corner is the origin of the page coordinate system
     *
     * @param topLeftX X coordinate of top-left corner
     * @param topLeftY Y coordinate of top-left corner
     * @param width    width
     * @param height   height
     * @return this
     */
    public CT_PageArea setPhysicalBox(double topLeftX,
                                      double topLeftY,
                                      double width,
                                      double height) {
        ST_Box physicalBox = new ST_Box(topLeftX, topLeftY, width, height);
        return setPhysicalBox(physicalBox);
    }


    /**
     * [required]
     * 获取 physical page area the top-left corner is the origin of the page coordinate system
     *
     * @return physical page area
     */
    public ST_Box getPhysicalBox() {
        return ST_Box.getInstance(this.getOFDElementText("PhysicalBox"));
    }

    /**
     * [optional]
     * set display area
     * <p>
     * the area actually displayed or printed on the page, located within the physical page area,
     * contains header, footer, body content, etc.
     * <p>
     * [exception handling] if the display area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the display area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @param applicationBox display area
     * @return this
     */
    public CT_PageArea setApplicationBox(ST_Box applicationBox) {
        this.setOFDEntity("ApplicationBox", applicationBox.toString());
        return this;
    }

    /**
     * [optional]
     * set display area
     * <p>
     * the area actually displayed or printed on the page, located within the physical page area,
     * contains header, footer, body content, etc.
     * <p>
     * [exception handling] if the display area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the display area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @param topLeftX X coordinate of top-left corner
     * @param topLeftY Y coordinate of top-left corner
     * @param width    width
     * @param height   height
     * @return this
     */
    public CT_PageArea setApplicationBox(double topLeftX,
                                         double topLeftY,
                                         double width,
                                         double height) {
        ST_Box applicationBox = new ST_Box(topLeftX, topLeftY, width, height);
        return setApplicationBox(applicationBox);
    }

    /**
     * [optional]
     * 获取 display area
     * <p>
     * the area actually displayed or printed on the page, located within the physical page area,
     * contains header, footer, body content, etc.
     * <p>
     * [exception handling] if the display area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the display area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @return display area
     */
    public ST_Box getApplicationBox() {
        return ST_Box.getInstance(this.getOFDElementText("ApplicationBox"));
    }

    /**
     * [optional]
     * set body area
     * <p>
     * the body area of the file, located within the display area.
     * the top-left corner coordinate determines its position in the display area
     * <p>
     * [exception handling] if the body area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the body area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @param contentBox body area
     * @return this
     */
    public CT_PageArea setContentBox(ST_Box contentBox) {
        this.setOFDEntity("ContentBox", contentBox);
        return this;
    }

    /**
     * [optional]
     * set body area
     * <p>
     * the body area of the file, located within the display area.
     * the top-left corner coordinate determines its position in the display area
     * <p>
     * [exception handling] if the body area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the body area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @param topLeftX X coordinate of top-left corner
     * @param topLeftY Y coordinate of top-left corner
     * @param width    width
     * @param height   height
     * @return this
     */
    public CT_PageArea setContentBox(double topLeftX,
                                     double topLeftY,
                                     double width,
                                     double height) {
        ST_Box contentBox = new ST_Box(topLeftX, topLeftY, width, height);
        return setContentBox(contentBox);
    }

    /**
     * [optional]
     * 获取 body area
     * <p>
     * the body area of the file, located within the display area.
     * the top-left corner coordinate determines its position in the display area
     * <p>
     * [exception handling] if the body area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the body area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @return body area
     */
    public ST_Box getContentBox() {
        return ST_Box.getInstance(this.getOFDElementText("ContentBox"));
    }

    /**
     * [optional]
     * set bleed area
     * <p>
     * extra bleed area beyond device capability limits, located outside the physical page area.
     * when absent, the default value is the physical page area
     * <p>
     * [exception handling] if the bleed area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the bleed area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @param bleedBox bleed area
     * @return this
     */
    public CT_PageArea setBleedBox(ST_Box bleedBox) {
        this.setOFDEntity("BleedBox", bleedBox);
        return this;
    }

    /**
     * [optional]
     * set bleed area
     * <p>
     * extra bleed area beyond device capability limits, located outside the physical page area.
     * when absent, the default value is the physical page area
     * <p>
     * [exception handling] if the bleed area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the bleed area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @param topLeftX X coordinate of top-left corner
     * @param topLeftY Y coordinate of top-left corner
     * @param width    width
     * @param height   height
     * @return this
     */
    public CT_PageArea setBleedBox(double topLeftX,
                                   double topLeftY,
                                   double width,
                                   double height) {
        ST_Box bleedBox = new ST_Box(topLeftX, topLeftY, width, height);
        return setBleedBox(bleedBox);
    }

    /**
     * [optional]
     * 获取 bleed area
     * <p>
     * extra bleed area beyond device capability limits, located outside the physical page area.
     * when absent, the default value is the physical page area
     * <p>
     * [exception handling] if the bleed area is not entirely within the physical page area,
     * the part outside the physical page area is ignored. If the bleed area is entirely outside the physical page area,
     * set the page as a blank page.
     *
     * @return bleed area
     */
    public ST_Box getBleedBox() {
        return ST_Box.getInstance(this.getOFDElementText("BleedBox"));
    }


    /**
     * 尝试获取页面最大区域
     * <p>
     * PhysicalBox | ApplicationBox| ContentBox
     * </p>
     * @return 区域
     */
    public ST_Box getBox() {
        ST_Box res = this.getPhysicalBox();
        if (res == null) {
            res = this.getApplicationBox();
        }
        if (res == null) {
            res = this.getContentBox();
        }
        return res;
    }
}
