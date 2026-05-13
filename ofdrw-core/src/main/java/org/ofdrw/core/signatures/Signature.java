package org.ofdrw.core.signatures;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * number签名或安全seal/signature在类表中的注册信息，依次签名或seal/signature对应一个节点
 * <p>
 * 18.1 Signature List - Figure 85 Table 66
 *
 * @author Quan Guanyu
 * @since 2019-11-20 06:53:11
 */
public class Signature extends OFDElement {
    public Signature(Element proxy) {
        super(proxy);
    }

    public Signature() {
        super("Signature");
    }

    /**
     * [required attribute]
     * 设置 签名或seal/signature的标识
     * <p>
     * recommended encoding: "sNNN" where NNN starts from 1
     *
     * @param id 签名或seal/signature的标识
     * @return this
     */
    public Signature setID(String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("签名或seal/signature的标识（ID）为空");
        }
        this.addAttribute("ID", id);
        return this;
    }

    /**
     * [required attribute]
     * 获取 签名或seal/signature的标识
     * <p>
     * recommended encoding: "sNNN" where NNN starts from 1
     *
     * @return 签名或seal/signature的标识
     */
    public String getID() {
        String str = this.attributeValue("ID");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("签名或seal/signature的标识（ID）为空");
        }
        return str;
    }

    /**
     * [optional attribute]
     * 设置  签名节点的类型
     * <p>
     * 可选值参考{@link SigType}
     * <p>
     * 默认值为Seal
     *
     * @param type 签名节点的类型
     * @return this
     */
    public Signature setType(SigType type) {
        if (type == null) {
            this.removeAttr("Type");
            return this;
        }
        this.addAttribute("Type", type.toString());
        return this;
    }

    /**
     * [optional attribute]
     * 获取  签名节点的类型
     * <p>
     * 可选值参考{@link SigType}
     * <p>
     * 默认值为Seal
     *
     * @return 签名节点的类型
     */
    public SigType getType() {
        return SigType.getInstance(this.attributeValue("Type"));
    }

    /**
     * [optional attribute, OFD 2.0]
     * 设置 基于的signature ID
     * <p>
     * 此签名基于签名标识，一旦签名标注了该属性，则验证时应同时验证“基”签名。
     *
     * @param id 基于的signature ID，如果为null表示需要删除该属性。
     * @return this
     */
    public Signature setRelative(String id) {
        if (id == null) {
            this.removeAttr("Relative");
            return this;
        }
        this.addAttribute("Relative", id);
        return this;
    }

    /**
     * [optional attribute, OFD 2.0]
     * 获取 基于的signature ID
     * <p>
     * 此签名基于签名标识，一旦签名标注了该属性，则验证时应同时验证“基”签名。
     *
     * @return 基于的signature ID，可能为空
     */
    public String getRelative() {
        return this.attributeValue("Relative");
    }

    /**
     * [required attribute]
     * 设置 points to包内的签名描述文件
     *
     * @param baseLoc points to包内的签名描述文件
     * @return this
     */
    public Signature setBaseLoc(ST_Loc baseLoc) {
        if (baseLoc == null) {
            throw new IllegalArgumentException("points to包内的签名描述文件（BaseLoc）为空");
        }
        this.addAttribute("BaseLoc", baseLoc.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 points to包内的签名描述文件
     *
     * @return points to包内的签名描述文件
     */
    public ST_Loc getBaseLoc() {
        String str = this.attributeValue("BaseLoc");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("points to包内的签名描述文件（BaseLoc）为空");
        }
        return ST_Loc.getInstance(str);
    }

}
