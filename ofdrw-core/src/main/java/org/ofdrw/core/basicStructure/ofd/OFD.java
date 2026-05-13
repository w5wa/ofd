package org.ofdrw.core.basicStructure.ofd;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

import java.util.List;

/**
 * 主入口
 * <p>
 * OFD.xml
 * ————《GB/T 33190-2016》 图 3
 */
public class OFD extends OFDElement {
    /**
     * [required]
     * 文件格式的版本号
     * <p>
     * 固定值： 1.2
     * <p>
     * 参照表 3
     */
    public static final String VERSION = "1.2";

    /**
     * [required]
     * 文件格式子集类型，取值为“OFD”，表明此文件符合本标准。
     */
    public static final String DOC_TYPE = "OFD";

    public OFD(Element proxy) {
        super(proxy);

    }

    public OFD() {
        super("OFD");
        this.addAttribute("Version", VERSION);
        this.addAttribute("DocType", DOC_TYPE);
    }


    /**
     * file object entry列表创建document object
     *
     * @param docBodies file object entry序列
     */
    public OFD(List<DocBody> docBodies) {
        this();
        for (DocBody item : docBodies) {
            if (item != null) {
                this.add(item);
            }
        }
    }


    /**
     * file object entry创建document object
     *
     * @param docBody file object entry
     */
    public OFD(DocBody docBody) {
        this();
        this.add(docBody);
    }

    /**
     * 【必选 属性】文件格式版本号
     *
     * @return 文件格式版本号
     */
    public String getVersion() {
        return this.attributeValue("Version");
    }

    /**
     * 【必选 属性】设置 文件版本号
     *
     * @param version 版本好
     * @return this
     */
    public OFD setVersion(String version) {
        this.addAttribute("Version", version);
        return this;
    }

    /**
     * 【必选 属性】文件格式子集类型，取值为“OFD”，表明此文件符合本标准。
     *
     * @return OFD
     */
    public String getDocType() {
        return DOC_TYPE;
    }

    /**
     * 【必选】增加file object entry。
     * file object entry; multiple can exist to include multiple layout documents in one file
     *
     * @param docBody file object entry
     * @return this
     */
    public OFD addDocBody(DocBody docBody) {
        this.add(docBody);
        return this;
    }

    /**
     * 【必选】 获取第一个文档入口
     *
     * @return file object entry (returns the first if multiple exist)
     */
    public DocBody getDocBody() {
        Element e = this.getOFDElement("DocBody");
        return e == null ? null : new DocBody(e);
    }

    /**
     * 获取指定序号的文档
     *
     * @param num 文档序号，从0起
     * @return file object entry (returns the first if multiple exist)
     */
    public DocBody getDocBody(int num) {
        return getDocBodies().get(num);
    }


    /**
     * 获取所有文档入口
     *
     * @return 所有文档入口
     */
    public List<DocBody> getDocBodies() {
        return this.getOFDElements("DocBody", DocBody::new);
    }

    @Override
    public String getQualifiedName() {
        return "ofd:OFD";
    }
}
