package org.ofdrw.core.basicStructure.ofd;

import org.dom4j.Element;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.ofd.docInfo.CT_DocInfo;
import org.ofdrw.core.versions.Versions;

/**
 * file object entry; multiple can exist to include multiple layout documents in one file
 */
public class DocBody extends OFDElement {

    /**
     * 文档root node文档名称
     */
    public static final String DOC_ROOT = "DocRoot";

    public DocBody(Element proxy) {
        super(proxy);
    }

    public DocBody() {
        super("DocBody");
    }


    /**
     * [required]
     * 设置文档元数据信息描述
     *
     * @param CTDocInfo 文档元数据信息描述
     * @return this
     */
    public DocBody setDocInfo(CT_DocInfo CTDocInfo) {
        this.set(CTDocInfo);
        return this;
    }

    /**
     * [required]
     * 获取文档元数据信息描述
     *
     * @return 文档元数据信息描述 或null
     */
    public CT_DocInfo getDocInfo() {
        Element element = this.getOFDElement("DocInfo");
        return element == null ? null : new CT_DocInfo(element);
    }

    /**
     * [optional]
     * 设置points to文档root node文档
     *
     * @param docRoot points toroot node文档路径
     * @return this
     */
    public DocBody setDocRoot(ST_Loc docRoot) {
        this.set(docRoot.getElement(DOC_ROOT));
        return this;
    }

    /**
     * [optional]
     * 获取points to文档root node文档路径
     *
     * @return points to文档root node文档路径
     */
    public ST_Loc getDocRoot() {
        String locStr = this.getOFDElementText("DocRoot");
        if (locStr == null || locStr.trim().length() == 0) {
            return null;
        }
        return new ST_Loc(locStr);
    }

    /**
     * [optional]
     * 获取 contains多个版本描述节点，用于定义文件因注释和其他改动产生的版本信息，见第19章
     *
     * @param versions 版本序列
     * @return this
     */
    public DocBody setVersions(Versions versions) {
        this.set(versions);
        return this;
    }

    /**
     * [optional]
     * 获取 contains多个版本描述序列
     *
     * @return contains多个版本描述序列
     */
    public Versions getVersions() {
        Element element = this.getOFDElement("Versions");
        return element == null ? null : new Versions(element);
    }

    /**
     * [optional]
     * 设置 points to该文档中签名和seal/signature结构的路径 （见18章）
     *
     * @param signatures points to该文档中签名和seal/signature结构的路径
     * @return this
     */
    public DocBody setSignatures(ST_Loc signatures) {
        this.setOFDEntity("Signatures", signatures.toString());
        return this;
    }

    /**
     * [optional]
     * 获取 points to该文档中签名和seal/signature结构的路径
     *
     * @return points to该文档中签名和seal/signature结构的路径
     */
    public ST_Loc getSignatures() {
        String locStr = this.getOFDElementText("Signatures");
        if (locStr == null || locStr.trim().isEmpty()) {
            return null;
        }
        return new ST_Loc(locStr);
    }


    @Override
    public String getQualifiedName() {
        return "ofd:DocBody";
    }
}
