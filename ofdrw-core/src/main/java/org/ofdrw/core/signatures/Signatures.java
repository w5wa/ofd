package org.ofdrw.core.signatures;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.doc.CT_CommonData;

import java.util.List;

/**
 * 签名列表root node
 * <p>
 * 签名列表问价你的入口点在 7.4 主入口中定义。
 * 签名列表文件中可以contains多个签名（例如联合发文等情况），见图 85。
 * 当允许下次继续添加签名时，该文件不会被contains到本次签名的
 * 保护文件列表（References）中。
 * <p>
 * 18.1 Signature List - Figure 85 Table 66
 *
 * @author Quan Guanyu
 * @since 2019-11-20 06:45:45
 */
public class Signatures extends OFDElement {
    public Signatures(Element proxy) {
        super(proxy);
    }

    public Signatures() {
        super("Signatures");
    }

    /**
     * [optional attribute]
     * 设置 安全标识的最大值
     * <p>
     * 作用与文档入口文件 Document.xml 中的 MaxID相同，
     * 为了避免在签名时影响文档入口文件，采用了与ST_ID不一样
     * 的ID编码方式。
     * <p>
     * recommended encoding: "sNNN" where NNN starts from 1
     *
     * @param maxSignId 安全标识的最大值
     * @return this
     */
    public Signatures setMaxSignId(String maxSignId) {
        this.removeOFDElemByNames("MaxSignId");
        if (maxSignId != null) {
            this.addOFDEntity("MaxSignId", maxSignId);
        }
        return this;
    }

    /**
     * [optional attribute]
     * 获取 安全标识的最大值
     * <p>
     * 作用与文档入口文件 Document.xml 中的 MaxID相同，
     * 为了避免在签名时影响文档入口文件，采用了与ST_ID不一样
     * 的ID编码方式。
     * <p>
     * recommended encoding: "sNNN" where NNN starts from 1
     *
     * @return 安全标识的最大值
     */
    public String getMaxSignId() {
        Element e = this.getOFDElement("MaxSignId");
        return e == null ? null :e.getStringValue();
    }

    /**
     * [optional]
     * 增加 number签名或安全seal/signature在类表中的注册信息
     *
     * @param signature number签名或安全seal/signature在类表中的注册信息
     * @return this
     */
    public Signatures addSignature(Signature signature) {
        if (signature == null) {
            return this;
        }
        this.add(signature);
        return this;
    }

    /**
     * [optional]
     * 获取 number签名或安全seal/signature在类表中的注册信息序列
     *
     * @return number签名或安全seal/signature在类表中的注册信息序列
     */
    public List<Signature> getSignatures() {
        return this.getOFDElements("Signature", Signature::new);
    }
}
