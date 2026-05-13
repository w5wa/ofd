package org.ofdrw.sign.verify.container;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.sign.signContainer.GBT35275DSContainer;
import org.ofdrw.sign.verify.SignedDataValidateContainer;
import org.ofdrw.sign.verify.exceptions.InvalidSignedValueException;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;

/**
 * digital signature verification container
 *  @deprecated OFD的number签名应符合 《GB/T 35275》  {@link GBT35275ValidateContainer}
 *
 * @author Quan Guanyu
 * @since 2020-04-22 03:22:22
 */
@Deprecated
public class DigitalValidateContainer implements SignedDataValidateContainer {
    /**
     * 验证使用的public key
     */
    public PublicKey pk;

    public DigitalValidateContainer(PublicKey pk) {
        if (pk == null) {
            throw new IllegalArgumentException("验证使用的public key参数(pk)不能为空");
        }
        this.pk = pk;
    }

    public DigitalValidateContainer(Certificate certificate) {
        this(certificate.getPublicKey());
    }

    @Override
    public void validate(SigType type, String alg, byte[] tbsContent, byte[] signedValue)
            throws InvalidSignedValueException, GeneralSecurityException {
        if (type != SigType.Sign) {
            throw new IllegalArgumentException("签名类型(type)必须是 Sign，不支持电子seal/stamp验证");
        }
        Signature sg = Signature.getInstance(alg, new BouncyCastleProvider());
        sg.initVerify(pk);
        sg.update(tbsContent);
        if (!sg.verify(signedValue)) {
            throw new InvalidSignedValueException("signature value不一致");
        }
    }
}
