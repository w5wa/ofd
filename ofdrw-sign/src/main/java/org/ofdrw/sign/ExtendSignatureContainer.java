package org.ofdrw.sign;


import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.ofdrw.core.signatures.SigType;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 * 扩展number签名容器
 *
 * @author Quan Guanyu
 * @since 2020-04-18 12:09:57
 */
public interface ExtendSignatureContainer {

    /**
     * provide file digest algorithm function
     *
     * @return digest algorithm function
     */
    MessageDigest getDigestFnc();

    /**
     * signature methodOID
     *
     * @return signature algorithm OID
     */
    ASN1ObjectIdentifier getSignAlgOID();

    /**
     * sign the data to be signed
     * <p>
     * 在操作过程中请勿对流进行关闭
     *
     * @param inData       data stream to be signed
     * @param propertyInfo seal/signature attribute information
     * @return signature or seal result value
     * @throws IOException              stream operation exception
     * @throws GeneralSecurityException signature computation exception
     */
    byte[] sign(InputStream inData, String propertyInfo) throws IOException, GeneralSecurityException;

    /**
     * get electronic seal binary encoding
     * <p>
     * if {@link #getSignType()} returns type {@link SigType#Sign}, return null
     *
     * @return electronic seal binary encoding
     * @throws IOException IO exception while retrieving seal
     */
    byte[] getSeal() throws IOException;

    /**
     * get signature node type
     *
     * @return signature node type
     */
    SigType getSignType();
}
