package org.ofdrw.sign.signContainer;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.sign.ExtendSignatureContainer;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;

/**
 * 国密SM2withSM3number签名实现容器
 * @deprecated OFD的number签名应符合 《GB/T 35275》  {@link GBT35275DSContainer}
 *
 * @author Quan Guanyu
 * @since 2020-04-20 12:26:33
 */
@Deprecated
public class DigitalSignContainer implements ExtendSignatureContainer {

    /**
     * signing private key
     */
    private final PrivateKey prvKey;

    public DigitalSignContainer(PrivateKey prvKey) {
        if (prvKey == null) {
            throw new IllegalArgumentException("签名使用private key（prvKey）不能为空");
        }
        this.prvKey = prvKey;
    }

    /**
     * SM3 digest algorithm function
     *
     * @return SM3 digest algorithm function
     */
    @Override
    public MessageDigest getDigestFnc() {
        return new SM3.Digest();
    }

    /**
     * SM2WithSM3
     *
     * @return signature algorithm OID
     */
    @Override
    public ASN1ObjectIdentifier getSignAlgOID() {
        return GMObjectIdentifiers.sm2sign_with_sm3;
    }

    /**
     * sign the data to be signed
     *
     * @param inData       data stream to be signed
     * @param propertyInfo ignored
     * @return signature result value
     * @throws IOException       IO stream read exception
     * @throws SecurityException signature computation exception
     */
    @Override
    public byte[] sign(InputStream inData, String propertyInfo) throws GeneralSecurityException, IOException {
        Signature signatureFnc = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString(),
                new BouncyCastleProvider());
        signatureFnc.initSign(prvKey);
        signatureFnc.update(IOUtils.toByteArray(inData));
        return signatureFnc.sign();
    }

    /**
     * electronic signature does not provide seal
     *
     * @return null
     * @throws IOException IO exception while retrieving seal
     */
    @Override
    public byte[] getSeal() throws IOException {
        return null;
    }

    /**
     * get signature node type
     *
     * @return signature node type
     */
    @Override
    public SigType getSignType() {
        return SigType.Sign;
    }
}
