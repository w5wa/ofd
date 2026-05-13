package org.ofdrw.core.signatures.sig;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.OFDSimpleTypeElement;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * 签名描述文件的root node
 * <p>
 * OFD的number签名通过对描述文件的保护间接实现对OFD原文的保护。
 * 签名结构中的签名信息（SignedInfo）是这一过程中的关键点，
 * 其中记录了当次number签名保护的所有文件的二进制摘要信息，同时
 * 将安全算法提供者、签名算法、签名时间、和所应用的安全seal/stamp等
 * 信息也contains在此节点内。签名描述文件同时contains了signature value将要存放的
 * 包内位置，一旦对该文件实施签名保护，则其对应的包内文件原文
 * 以及本次签名对应的附加信息都将不可改动，从而实现依次number签名
 * 对整个原文内容的保护。签名描述文件的主要结构描述见图 86。
 * <p>
 * 文件摘要file root node为 Signature，其子节点 SignedInfo 对应元素说明见表 67。
 * <p>
 * 18.2.1 File Digest - Figure 86 Table 67
 *
 * @author Quan Guanyu
 * @since 2019-11-20 07:12:16
 */
public class Signature extends OFDElement {
    public Signature(Element proxy) {
        super(proxy);
    }

    public Signature() {
        super("Signature");
    }


    /**
     * [required]
     * 设置 签名要保护的原文及本次签名的相关信息
     *
     * @param signedInfo 签名要保护的原文及本次签名的相关信息
     * @return this
     */
    public Signature setSignedInfo(SignedInfo signedInfo) {
        if (signedInfo == null) {
            throw new IllegalArgumentException("签名要保护的原文及本次签名的相关信息（SignedInfo）为空");
        }
        this.set(signedInfo);
        return this;
    }

    /**
     * [required]
     * 获取 签名要保护的原文及本次签名的相关信息
     *
     * @return 签名要保护的原文及本次签名的相关信息
     */
    public SignedInfo getSignedInfo() {
        Element e = this.getOFDElement("SignedInfo");
        if (e == null) {
            throw new IllegalArgumentException("签名要保护的原文及本次签名的相关信息（SignedInfo）为空");
        }
        return new SignedInfo(e);
    }

    /**
     * [required]
     * 设置 points to安全签名提供者所返还的针对签名描述文件计算所得的signature value文件
     *
     * @param signedValue points to安全签名提供者所返还的针对签名描述文件计算所得的signature value文件
     * @return this
     */
    public Signature setSignedValue(ST_Loc signedValue) {
        if (signedValue == null) {
            throw new IllegalArgumentException("");
        }
        this.set(new OFDSimpleTypeElement("SignedValue", signedValue));
        return this;
    }

    /**
     * [required]
     * 获取 points to安全签名提供者所返还的针对签名描述文件计算所得的signature value文件
     *
     * @return points to安全签名提供者所返还的针对签名描述文件计算所得的signature value文件
     */
    public ST_Loc getSignedValue() {
        Element e = this.getOFDElement("SignedValue");
        return e == null ? null : ST_Loc.getInstance(e);
    }
}
