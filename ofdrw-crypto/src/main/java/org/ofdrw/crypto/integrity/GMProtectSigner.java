package org.ofdrw.crypto.integrity;

import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.ofdrw.gm.sm2strut.ContentInfo;
import org.ofdrw.gm.sm2strut.OIDs;
import org.ofdrw.gm.sm2strut.SignedData;
import org.ofdrw.gm.sm2strut.builder.SignedDataBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;

/**
 * 以国密 SM2 和 SM3 算法实现的OFD完整性保护签名实现
 * <p>
 * signature value符合 《GB/T 35275》标准
 *
 * @author Quan Guanyu
 * @since 2021-08-23 19:07:54
 */
public class GMProtectSigner implements ProtectSigner {

    /**
     * 版式文件合成者的signing private key
     */
    private PrivateKey privateKey;

    /**
     * 版式文件合成者的public keycertificate
     */
    private Certificate signCert;

    /**
     * 完整性保护签名实现
     *
     * @param privateKey 版式文件合成者的signing private key
     * @param signCert   版式文件合成者的public keycertificate
     */
    public GMProtectSigner(@NotNull PrivateKey privateKey, @NotNull Certificate signCert) {
        if (privateKey == null) {
            throw new IllegalArgumentException("版式文件合成者的signing private key(privateKey)为空");
        }
        if (signCert == null) {
            throw new IllegalArgumentException("版式文件合成者的public keycertificate(signCert)为空");
        }
        this.privateKey = privateKey;
        this.signCert = signCert;
    }

    /**
     * 根据 GM/T 0099  OFD完整性保护协议 执行完整性保护 7.4.3 中的 c) d) 步骤
     * <p>
     * c) based on the signature scheme, calculate the hash value of the integrity protection file;
     * d) based on the signature scheme, use the layout file composer's signing private key to digitally sign the hash value;
     *
     * @param tbs 待签名file path
     * @return signature value应符合“GB/T 35275”标准
     * @throws GeneralSecurityException security computation exception
     */
    @Override
    public byte[] digestThenSign(Path tbs) throws GeneralSecurityException, IOException {
        // 杂凑算法采用SM3时，遵循《GB/32905》
        MessageDigest md = new SM3.Digest();
        final byte[] raw = Files.readAllBytes(tbs);
        md.update(raw);
        // 计算完整性保护文件的hash value,作为待签名原文。
        final byte[] plaintext = md.digest();
        // 签名算法采用SM2时，遵循 《GB/T32918》和《GB/T 35275》
        Signature sg = Signature.getInstance("SM3WithSM2", new BouncyCastleProvider());
        sg.initSign(this.privateKey);
        sg.update(plaintext);
        // 签名算法采用SM2时，遵循 《GB/T32918》
        final byte[] signature = sg.sign();
        // 构造 《GB/T 35275》数据格式
        final SignedData signedData = SignedDataBuilder.signedData(plaintext, signature, this.signCert);
        // DER编码ASN1结构
        final byte[] encoded = signedData.getEncoded();
        ContentInfo ci = new ContentInfo(OIDs.signedData, signedData);
//        System.out.println(        Base64.toBase64String(encoded));
        return ci.getEncoded();
    }
}
