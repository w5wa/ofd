package org.ofdrw.core.crypto.encryt;

import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * 明密文对应关系
 *
 * @author Quan Guanyu
 * @since 2021-07-15 19:20:24
 */
public class EncryptEntry extends OFDElement {
    public EncryptEntry(Element proxy) {
        super(proxy);
    }

    public EncryptEntry() {
        super("EncryptEntry");
    }

    /**
     * create plaintext-ciphertext mapping
     *
     * @param path           absolute path of the file in the package before encryption
     * @param ePath          absolute path of the ciphertext in the package after encryption
     * @param decryptSeedLoc path to the key data file unique to this ciphertext
     */
    public EncryptEntry(ST_Loc path, ST_Loc ePath, ST_Loc decryptSeedLoc) {
        this();
        setPathA(path);
        setEPath(ePath);
        setDecryptSeedLoc(decryptSeedLoc);
    }

    /**
     * create plaintext-ciphertext mapping
     *
     * @param path  absolute path of the file in the package before encryption
     * @param ePath absolute path of the ciphertext in the package after encryption
     */
    public EncryptEntry(ST_Loc path, ST_Loc ePath) {
        this();
        setPathA(path);
        setEPath(ePath);
    }

    /**
     * create plaintext-ciphertext mapping
     *
     * @param path           absolute path of the file in the package before encryption
     * @param ePath          absolute path of the ciphertext in the package after encryption
     * @param decryptSeedLoc path to the key data file unique to this ciphertext
     */
    public EncryptEntry(String path, String ePath, String decryptSeedLoc) {
        this();
        setPathA(path);
        setEPath(ePath);
        setDecryptSeedLoc(decryptSeedLoc);
    }

    /**
     * create plaintext-ciphertext mapping
     *
     * @param path  absolute path of the file in the package before encryption
     * @param ePath absolute path of the ciphertext in the package after encryption
     */
    public EncryptEntry(String path, String ePath) {
        this();
        setPathA(path);
        setEPath(ePath);
    }


    /**
     * [required attribute]
     * 设置 absolute path of file before encryption
     *
     * @param path absolute path of file before encryption
     * @return this
     */
    public EncryptEntry setPathA(@NotNull String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("absolute path of file before encryption(path)为空");
        }
        this.addAttribute("Path", path);
        return this;
    }

    /**
     * [required attribute]
     * 设置 absolute path of file before encryption
     * <p>
     * 区别于{@link #getPath()} 重名名为 PathA
     *
     * @param path absolute path of file before encryption
     * @return this
     */
    public EncryptEntry setPathA(@NotNull ST_Loc path) {
        return setPathA(path == null ? null : path.toString());
    }

    /**
     * [required attribute]
     * 获取 absolute path of file before encryption
     *
     * @return absolute path of file before encryption
     */
    public ST_Loc getPathA() {
        return ST_Loc.getInstance(this.attributeValue("Path"));
    }

    /**
     * [required attribute]
     * 设置 absolute path of ciphertext after encryption
     *
     * @param ePath absolute path of the ciphertext in the package after encryption
     * @return this
     */
    public EncryptEntry setEPath(@NotNull String ePath) {
        if (ePath == null || ePath.isEmpty()) {
            throw new IllegalArgumentException("absolute path of ciphertext after encryption(ePath)为空");
        }
        this.addAttribute("EPath", ePath);
        return this;
    }

    /**
     * [required attribute]
     * 设置 absolute path of ciphertext after encryption
     *
     * @param ePath absolute path of the ciphertext in the package after encryption
     * @return this
     */
    public EncryptEntry setEPath(@NotNull ST_Loc ePath) {
        return setEPath(ePath == null ? null : ePath.toString());
    }

    /**
     * [required attribute]
     * 获取 absolute path of ciphertext after encryption
     *
     * @return absolute path of ciphertext after encryption
     */
    public ST_Loc getEPath() {
        return ST_Loc.getInstance(this.attributeValue("EPath"));
    }


    /**
     * [optional attribute]
     * 设置 path to the key data file unique to this ciphertext
     * <p>
     * when this attribute is absent, the common key data defined in the encryption operation is used
     *
     * @param decryptSeedLoc path to the key data file unique to this ciphertext，null时表示删除
     * @return this
     */
    public EncryptEntry setDecryptSeedLoc(String decryptSeedLoc) {
        if (decryptSeedLoc == null) {
            this.removeAttr("DecryptSeedLoc");
            return this;
        }
        this.addAttribute("DecryptSeedLoc", decryptSeedLoc);
        return this;
    }

    /**
     * [optional attribute]
     * 设置 path to the key data file unique to this ciphertext
     * <p>
     * when this attribute is absent, the common key data defined in the encryption operation is used
     *
     * @param decryptSeedLoc path to the key data file unique to this ciphertext，null时表示删除
     * @return this
     */
    public EncryptEntry setDecryptSeedLoc(ST_Loc decryptSeedLoc) {
        return setDecryptSeedLoc(decryptSeedLoc == null ? null : decryptSeedLoc.toString());
    }

    /**
     * [optional attribute]
     * 获取 path to the key data file unique to this ciphertext
     * <p>
     * when this attribute is absent, the common key data defined in the encryption operation is used
     *
     * @return path to the key data file unique to this ciphertext，null请使用 加密操作信息中定义的通用key数据
     */
    @Nullable
    public ST_Loc getDecryptSeedLoc() {
        return ST_Loc.getInstance(this.attributeValue("DecryptSeedLoc"));
    }


}
