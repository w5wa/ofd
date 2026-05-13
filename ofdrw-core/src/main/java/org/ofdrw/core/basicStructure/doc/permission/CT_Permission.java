package org.ofdrw.core.basicStructure.doc.permission;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

/**
 * 本标准支持设置文档权限声明（Permission）节点，以达到文档防扩散等应用目的。
 * 文档权限声明结构如 图 9 所示。
 * <p>
 * 7.5 小节 CT_Permission
 *
 * @author Quan Guanyu
 * @since 2019-10-06 08:09:21
 */
public class CT_Permission extends OFDElement {
    public CT_Permission(Element proxy) {
        super(proxy);
    }

    public CT_Permission() {
        super("Permissions");
    }

    /**
     * [optional]
     * 设置 是否允许编辑
     * <p>
     * default value: true
     *
     * @param edit true - 允许编辑； false - 不允许编辑
     * @return this
     */
    public CT_Permission setEdit(boolean edit) {
        this.setOFDEntity("Edit", Boolean.toString(edit));
        return this;
    }

    /**
     * [optional]
     * 获取 是否允许编辑
     * <p>
     * default value: true
     *
     * @return true - 允许编辑； false - 不允许编辑
     */
    public Boolean getEdit() {
        String str = this.getOFDElementText("Edit");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional]
     * 设置 是否允许添加或修改标注
     * <p>
     * default value: true
     *
     * @param annot true - 允许添加或修改标注； false - 不允许添加或修改标注
     * @return this
     */
    public CT_Permission setAnnot(boolean annot) {
        this.setOFDEntity("Annot", Boolean.toString(annot));
        return this;
    }

    /**
     * [optional]
     * 获取 是否允许添加或修改标注
     * <p>
     * default value: true
     *
     * @return true - 允许添加或修改标注； false - 不允许添加或修改标注
     */
    public Boolean getAnnot() {
        String str = this.getOFDElementText("Annot");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }


    /**
     * [optional]
     * 设置 是否允许导出
     * <p>
     * default value: true
     *
     * @param export true - 允许导出； false - 不允许导出
     * @return this
     */
    public CT_Permission setExport(boolean export) {
        this.setOFDEntity("Export", Boolean.toString(export));
        return this;
    }

    /**
     * [optional]
     * 获取 是否允许导出
     * <p>
     * default value: true
     *
     * @return true - 允许导出； false - 不允许导出
     */
    public Boolean getExport() {
        String str = this.getOFDElementText("Export");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional]
     * 设置 是否允许进行number签名
     * <p>
     * default value: true
     *
     * @param signature true - 允许进行number签名； false - 不允许进行number签名
     * @return this
     */
    public CT_Permission setSignature(boolean signature) {
        this.setOFDEntity("Signature", Boolean.toString(signature));
        return this;
    }

    /**
     * [optional]
     * 获取 是否允许进行number签名
     * <p>
     * default value: true
     *
     * @return true - 允许进行number签名； false - 不允许进行number签名
     */
    public Boolean getSignature() {
        String str = this.getOFDElementText("Signature");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional]
     * 设置 是否允许添加水印
     * <p>
     * default value: true
     *
     * @param watermark true - 允许添加水印； false - 不允许添加水印
     * @return this
     */
    public CT_Permission setWatermark(boolean watermark) {
        this.setOFDEntity("Watermark", Boolean.toString(watermark));
        return this;
    }

    /**
     * [optional]
     * 获取 是否允许添加水印
     * <p>
     * default value: true
     *
     * @return true - 允许添加水印； false - 不允许添加水印
     */
    public Boolean getWatermark() {
        String str = this.getOFDElementText("Watermark");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional]
     * 设置 是否允许截屏
     * <p>
     * default value: true
     *
     * @param printScreen true - 允许截屏； false - 不允许截屏
     * @return this
     */
    public CT_Permission setPrintScreen(boolean printScreen) {
        this.setOFDEntity("PrintScreen", Boolean.toString(printScreen));
        return this;
    }

    /**
     * [optional]
     * 获取 是否允许截屏
     * <p>
     * default value: true
     *
     * @return true - 允许截屏； false - 不允许截屏
     */
    public Boolean getPrintScreen() {
        String str = this.getOFDElementText("PrintScreen");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional]
     * 设置 打印权限
     * <p>
     * specific permission and copy settings are controlled by attributes Printable and Copies. If the Print node is absent,
     * printing is allowed by default with no copy limit
     *
     * @param print 打印权限
     * @return this
     */
    public CT_Permission setPrint(Print print) {
        this.set(print);
        return this;
    }

    /**
     * [optional]
     * 获取 打印权限
     * <p>
     * specific permission and copy settings are controlled by attributes Printable and Copies. If the Print node is absent,
     * printing is allowed by default with no copy limit
     *
     * @return 打印权限
     */
    public Print getPrint() {
        Element e = this.getOFDElement("Print");
        return e == null ? null : new Print(e);
    }

    /**
     * [optional]
     * 设置 有效期
     * <p>
     * the access period for this document is determined by the start date and
     * end date; the start date cannot be later than the end date, and both start and end
     * dates must have at least one present. When start date is absent, there is no start date restriction;
     * when end date is absent, there is no end date restriction; when this node is absent,
     * neither start nor end date is restricted
     *
     * @param validPeriod 有效期
     * @return this
     */
    public CT_Permission setValidPeriod(ValidPeriod validPeriod) {
        this.set(validPeriod);
        return this;
    }

    /**
     * [optional]
     * 获取 有效期
     * <p>
     * the access period for this document is determined by the start date and
     * end date; the start date cannot be later than the end date, and both start and end
     * dates must have at least one present. When start date is absent, there is no start date restriction;
     * when end date is absent, there is no end date restriction; when this node is absent,
     * neither start nor end date is restricted
     *
     * @return 有效期
     */
    public ValidPeriod getValidPeriod() {
        Element e = this.getOFDElement("ValidPeriod");
        return e == null ? null : new ValidPeriod(e);
    }
}
