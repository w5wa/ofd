package org.ofdrw.core.crypto.encryt;

import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.crypto.ProtectionCaseID;

import java.util.List;

/**
 * key描述文件
 * <p>
 * key描述文件采用XML格式描述，存储了方案、算法和
 * 多人、多角色、多密码或certificate等关键解密信息，其数据结构见图C.1
 * <p>
 * 根据加密类型的不同，文件对称加密的包装key的生成方式也不同。
 * 口令加密是，使用用户输入的口令作为基础，通过key派生函数派生出key，
 * 然后用该key对文件对称加密key进行加密，生成文件对称加密的包装key。
 * 使用key派生函数是，应遵循GB/T 32918。
 * certificate加密时，使用用户的public keycertificate对文件对称加密key进行非对称加密，
 * 生成文件对称加密的包装key。
 * <p>
 * GMT 0099-2020 C.2 key描述文件
 *
 * @author Quan Guanyu
 * @since 2021-06-21 18:48:15
 */
public class DecyptSeed extends OFDElement {

    public DecyptSeed(Element proxy) {
        super(proxy);
    }

    public DecyptSeed() {
        super("DecyptSeed");
    }

    /**
     * [required attribute]
     * <p>
     * 设置 encryption operation identifier，应与解密入口描述中的一致
     *
     * @param id encryption operation identifier
     * @return this
     */
    public DecyptSeed setID(@NotNull String id) {
        if (id == null || id.length() == 0) {
            throw new IllegalArgumentException("encryption operation identifier(id)为空");
        }
        this.addAttribute("ID", id);
        return this;
    }

    /**
     * [required attribute]
     * <p>
     * 获取 encryption operation identifier，应与解密入口描述中的一致
     *
     * @return encryption operation identifier
     */
    @NotNull
    public String getID() {
        return this.attributeValue("ID");
    }

    /**
     * [required attribute]
     * 设置 encryption protection scheme identifier，参见附录A
     *
     * @param encryptCaseId encryption protection scheme identifier {@link ProtectionCaseID}
     * @return this
     */
    public DecyptSeed setEncryptCaseId(@NotNull String encryptCaseId) {
        if (encryptCaseId == null) {
            throw new IllegalArgumentException("encryption protection scheme identifier(id)为空");
        }
        this.attributeValue("EncryptCaseId", encryptCaseId);
        return this;
    }

    /**
     * [required attribute]
     * 设置 encryption protection scheme identifier，参见附录A
     *
     * @param encryptCaseId encryption protection scheme identifier {@link ProtectionCaseID}
     * @return this
     */
    public DecyptSeed setEncryptCaseId(@NotNull ProtectionCaseID encryptCaseId) {
        if (encryptCaseId == null) {
            throw new IllegalArgumentException("encryption protection scheme identifier(id)为空");
        }
        this.addAttribute("EncryptCaseId", encryptCaseId.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 encryption protection scheme identifier，参见附录A {@link ProtectionCaseID}
     *
     * @return encryption protection scheme identifier
     */
    public String getEncryptCaseId() {
        return this.attributeValue("EncryptCaseId");
    }

    /**
     * [required, OFD 2.0]
     * 增加 可解密该次操作的用户
     *
     * @param userInfo 可解密该次操作的用户
     * @return this
     */
    public DecyptSeed addUserInfo(@NotNull UserInfo userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException("可解密该次操作的用户(userInfo)为空");
        }
        this.add(userInfo);
        return this;
    }

    /**
     * [required, OFD 2.0]
     * 获取 可解密该次操作的用户列表
     *
     * @return 可解密该次操作的用户列表
     */
    public List<UserInfo> getUserInfos() {
        return this.getOFDElements("UserInfo", UserInfo::new);
    }


    /**
     * [required, OFD 2.0]
     * 设置 扩展参数节点
     *
     * @param extendParams 扩展参数节点
     * @return this
     */
    public DecyptSeed setExtendParams(@NotNull ExtendParams extendParams) {
        if (extendParams == null) {
            throw new IllegalArgumentException("扩展参数节点(ExtendParams)为空");
        }
        this.set(extendParams);
        return this;
    }

    /**
     * [required, OFD 2.0]
     * 获取 扩展参数节点
     *
     * @return 扩展参数节点
     */
    @NotNull
    public ExtendParams getExtendParams() {
        Element e = this.getOFDElement("ExtendParams");
        if (e == null) {
            ExtendParams emptyParams = new ExtendParams();
            this.setExtendParams(emptyParams);
            return emptyParams;
        }
        return new ExtendParams(e);
    }
}
