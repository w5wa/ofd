package org.ofdrw.core.signatures.range;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Loc;

import java.util.Base64;

/**
 * 针对一个文件的摘要节点
 * <p>
 * 18.2.2 签名的范围 图 87 表 68
 *
 * @author Quan Guanyu
 * @since 2019-11-21 18:01:53
 */
public class Reference extends OFDElement {
    public Reference(Element proxy) {
        super(proxy);
    }

    public Reference() {
        super("Reference");
    }

    public Reference(ST_Loc fileRef, byte[] checkValue) {
        this();
        this.setFileRef(fileRef)
                .setCheckValue(checkValue);
    }


    /**
     * [required attribute]
     * 设置 points to包内的文件，使用absolute path
     *
     * @param fileRef points to包内的文件，使用absolute path
     * @return this
     */
    public Reference setFileRef(ST_Loc fileRef) {
        if (fileRef == null) {
            throw new IllegalArgumentException("points to包内的文件（FileRef）为空");
        }
        this.addAttribute("FileRef", fileRef.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 points to包内的文件，使用absolute path
     *
     * @return points to包内的文件，使用absolute path
     */
    public ST_Loc getFileRef() {
        return ST_Loc.getInstance(this.attributeValue("FileRef"));
    }


    /**
     * [required]
     * 设置 对包内文件进行摘要计算值的hash value
     * <p>
     * 所得的二进制摘要值进行 base64 编码
     *
     * @param checkValue 对包内文件进行摘要计算值的hash value
     * @return this
     */
    public Reference setCheckValue(byte[] checkValue) {
        if (checkValue == null || checkValue.length == 0) {
            throw new IllegalArgumentException("摘要计算值（CheckValue）为空");
        }

        this.setOFDEntity("CheckValue", Base64.getEncoder().encodeToString(checkValue));
        return this;
    }

    /**
     * [required]
     * 获取 对包内文件进行摘要计算值的hash value
     *
     * @return 对包内文件进行摘要计算值的hash value
     */
    public byte[] getCheckValue() {
        Element e = this.getOFDElement("CheckValue");
        if (e == null) {
            throw new IllegalArgumentException("摘要计算值（CheckValue）为空");
        }
        return Base64.getDecoder().decode(e.getTextTrim());
    }


}
