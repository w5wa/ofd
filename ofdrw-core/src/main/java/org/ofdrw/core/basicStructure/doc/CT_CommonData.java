package org.ofdrw.core.basicStructure.doc;

import org.dom4j.Element;
import org.ofdrw.core.DefaultElementProxy;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.pageObj.CT_TemplatePage;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.basicType.ST_RefID;

import java.util.List;
import java.util.stream.Collectors;

/**
 * document common data structure
 * <p>
 * ————《GB/T 33190-2016》 图 6
 *
 * @author Quan Guanyu
 * @since 2019-10-04 07:46:11
 */
public class CT_CommonData extends OFDElement {
    public CT_CommonData(Element proxy) {
        super(proxy);
    }

    public CT_CommonData() {
        super("CommonData");
    }

    /**
     * [required]
     * set maximum identifier value for all objects in the current document.
     * initial value: 0. MaxUnitID is mainly used for document editing;
     * when adding a new object to the document, a new
     * identifier must be allocated; the new identifier should be MaxUnitID + 1,
     * and this MaxUnitID value must be updated accordingly.
     *
     * @param maxUnitID maximum object identifier value
     * @return this
     */
    public CT_CommonData setMaxUnitID(ST_ID maxUnitID) {
        this.setOFDEntity("MaxUnitID", maxUnitID);
        return this;
    }

    /**
     * [required]
     * set maximum identifier value for all objects in the current document.
     * initial value: 0. MaxUnitID is mainly used for document editing;
     * when adding a new object to the document, a new
     * identifier must be allocated; the new identifier should be MaxUnitID + 1,
     * and this MaxUnitID value must be updated accordingly.
     *
     * @param maxUnitID maximum object identifier value
     * @return this
     */
    public CT_CommonData setMaxUnitID(long maxUnitID) {
        return setMaxUnitID(new ST_ID(maxUnitID));
    }

    /**
     * [required]
     * 获取 当前文档中所有对象使用标识的最大值
     *
     * @return 当前文档中所有对象使用标识的最大值0
     */
    public ST_ID getMaxUnitID() {
        return ST_ID.getInstance(this.getOFDElementText("MaxUnitID"));
    }

    /**
     * [required]
     * 设置 该文档page area的默认大小和位置
     *
     * @param pageArea 文档page area的默认大小和位置
     * @return this
     */
    public CT_CommonData setPageArea(CT_PageArea pageArea) {
        this.set(pageArea);
        return this;
    }

    /**
     * [required]
     * 获取 该文档page area的默认大小和位置
     *
     * @return 该文档page area的默认大小和位置
     */
    public CT_PageArea getPageArea() {
        Element e = this.getOFDElement("PageArea");
        return e == null ? null : new CT_PageArea(e);
    }

    /**
     * [optional]
     * 设置 public resource sequence 路径 (如果已经存在PublicRes那么替换)
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys see section 7.9; glyphs, color spaces, etc. should be described in public resource files
     *
     * @param publicRes public resource sequence
     * @return this
     */
    public CT_CommonData setPublicRes(ST_Loc publicRes) {
        this.setOFDEntity("PublicRes", publicRes);
        return this;
    }

    /**
     * [optional]
     * 获取 public resource sequence(列表中的第一个PublicRes)
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys see section 7.9; glyphs, color spaces, etc. should be described in public resource files
     *
     * @return public resource sequence path
     */
    public ST_Loc getPublicRes() {
        return ST_Loc.getInstance(this.getOFDElementText("PublicRes"));
    }

    /**
     * [optional]
     * 添加 public resource sequence 路径
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys see section 7.9; glyphs, color spaces, etc. should be described in public resource files
     *
     * @param publicRes public resource sequence
     * @return this
     */
    public CT_CommonData addPublicRes(ST_Loc publicRes) {
        if (publicRes == null || publicRes.getLoc() == null) {
            return this;
        }
        this.addOFDEntity("PublicRes", publicRes);
        return this;
    }


    /**
     * [optional]
     * 获取 public resource 序列
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys see section 7.9; glyphs, color spaces, etc. should be described in public resource files
     * <p>
     * PublicRes 数量为 0~n
     *
     * @return public resource sequence path
     */
    public List<ST_Loc> getPublicResList() {
        return this.getOFDElements("PublicRes", OFDElement::new)
                .stream()
                .map(DefaultElementProxy::getText)
                .map(ST_Loc::getInstance).collect(Collectors.toList());
    }

    /**
     * [optional]
     * 设置 file resource sequence path（DocumentRes已经存在那么替换）
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys, see section 7.9,
     * drawing parameters, multimedia, vector images, etc. should be described in file resource files
     *
     * @param documentRes public resource sequence
     * @return this
     */
    public CT_CommonData setDocumentRes(ST_Loc documentRes) {
        this.setOFDEntity("DocumentRes", documentRes);
        return this;
    }

    /**
     * [optional]
     * 添加 file resource sequence path
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys, see section 7.9,
     * drawing parameters, multimedia, vector images, etc. should be described in file resource files
     *
     * @param documentRes public resource sequence
     * @return this
     */
    public CT_CommonData addDocumentRes(ST_Loc documentRes) {
        if (documentRes == null || documentRes.getLoc() == null) {
            return this;
        }
        this.addOFDEntity("DocumentRes", documentRes);
        return this;
    }

    /**
     * [optional]
     * 获取 file resource sequence path
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys, see section 7.9,
     * drawing parameters, multimedia, vector images, etc. should be described in file resource files
     *
     * @return file resource sequence path
     */
    public ST_Loc getDocumentRes() {
        return ST_Loc.getInstance(this.getOFDElementText("DocumentRes"));
    }

    /**
     * [optional]
     * 获取 文件资源序列
     * <p>
     * public resource sequence; each node points to a resource description file within the OFD package,
     * for source description keys, see section 7.9,
     * drawing parameters, multimedia, vector images, etc. should be described in file resource files
     * <p>
     * DocumentRes 数量为 0~n
     *
     * @return file resource sequence path
     */
    public List<ST_Loc> getDocumentResList() {
        return this.getOFDElements("DocumentRes", OFDElement::new)
                .stream()
                .map(DefaultElementProxy::getText)
                .map(ST_Loc::getInstance).collect(Collectors.toList());
    }

    /**
     * [optional]
     * 增加 模板页序列
     * <p>
     * a collection of template pages with the same content structure as normal pages; see section 7.7
     *
     * @param templatePage 模板页序列
     * @return this
     */
    public CT_CommonData addTemplatePage(CT_TemplatePage templatePage) {
        this.add(templatePage);
        return this;
    }

    /**
     * [optional]
     * 获取 模板页序列
     * <p>
     * a collection of template pages with the same content structure as normal pages; see section 7.7
     *
     * @return 模板页序列 (可能为空容器)
     */
    public List<CT_TemplatePage> getTemplatePages() {
        return this.getOFDElements("TemplatePage", CT_TemplatePage::new);
    }

    /**
     * [optional]
     * 设置 引用在resource file中定义的颜色标识符
     * <p>
     * see section 8.3.1 for color space description; if absent, RGB is used as default
     *
     * @param defaultCS color space引用
     * @return this
     */
    public CT_CommonData setDefaultCS(ST_RefID defaultCS) {
        this.setOFDEntity("DefaultCS", defaultCS);
        return this;
    }

    /**
     * [optional]
     * 获取 引用在resource file中定义的颜色标识符
     * <p>
     * see section 8.3.1 for color space description; if absent, RGB is used as default
     *
     * @return color space引用
     */
    public ST_RefID getDefaultCS() {
        return ST_RefID.getInstance(this.getOFDElementText("DefaultCS"));
    }
}
