package org.ofdrw.core.crypto.encryt;

import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ofdrw.core.OFDElement;

import java.util.List;

/**
 * 未加密的名密文映射表文件数据结构
 * <p>
 * GMT0099 附录C 图C.3
 *
 * @author Quan Guanyu
 * @since 2021-07-15 19:14:42
 */
public class EncryptEntries extends OFDElement {
    public EncryptEntries(Element proxy) {
        super(proxy);
    }

    public EncryptEntries() {
        super("EncryptEntries");
    }

    /**
     * [required attribute]
     * 设置 encryption operation identifier
     * <p>
     * 应与解密入口描述中的一致
     *
     * @param id encryption operation identifier，应与解密入口描述中的一致
     * @return this
     */
    public EncryptEntries setID(@NotNull String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("encryption operation identifier(id)为空");
        }
        this.addAttribute("ID", id);
        return this;
    }

    /**
     * [required attribute]
     * 获取 encryption operation identifier
     *
     * @return encryption operation identifier
     */
    public String getID() {
        return this.attributeValue("ID");
    }

    /**
     * [required]
     * add plaintext-ciphertext mapping
     *
     * @param encryptEntry 明密文对应关系
     * @return this
     */
    public EncryptEntries addEncryptEntry(EncryptEntry encryptEntry) {
        if (encryptEntry == null) {
            return this;
        }
        this.add(encryptEntry);
        return this;
    }

    /**
     * [required]
     * add plaintext-ciphertext mapping
     *
     * @param path           absolute path of the file in the package before encryption
     * @param ePath          absolute path of the ciphertext in the package after encryption
     * @param decryptSeedLoc path to the key data file unique to this ciphertext
     * @return this
     */
    public EncryptEntries addEncryptEntry(@NotNull String path, @NotNull String ePath, @Nullable String decryptSeedLoc) {
        this.add(new EncryptEntry(path, ePath, decryptSeedLoc));
        return this;
    }

    /**
     * [required]
     * add plaintext-ciphertext mapping
     *
     * @param path  absolute path of the file in the package before encryption
     * @param ePath absolute path of the ciphertext in the package after encryption
     * @return this
     */
    public EncryptEntries addEncryptEntry(@NotNull String path, @NotNull String ePath) {
        this.add(new EncryptEntry(path, ePath));
        return this;
    }

    /**
     * [required]
     * 获取 明密文对应关系列表
     *
     * @return 明密文对应关系列表
     */
    public List<EncryptEntry> getEncryptEntries() {
        return this.getOFDElements("EncryptEntry", EncryptEntry::new);
    }
}
