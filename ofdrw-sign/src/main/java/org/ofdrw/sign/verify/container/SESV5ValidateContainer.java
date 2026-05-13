package org.ofdrw.sign.verify.container;

import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.gm.ses.v5.SES_Signature;
import org.ofdrw.gm.ses.v5.TBS_Sign;
import org.ofdrw.sign.verify.SignedDataValidateContainer;
import org.ofdrw.sign.verify.exceptions.InvalidSignedValueException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Arrays;

/**
 * V5电子seal/signature数据验证容器
 * <p>
 * 签名计算范围：seal/signature签名=Sign(TBS_Sign)
 * <p>
 * Note: for testing only. Electronic seal verification should follow nationally compliant procedures.
 *
 * @since 2026-04-24
 * @author minghu.zhang
 */
public class SESV5ValidateContainer implements SignedDataValidateContainer {

    @Override
    public void validate(SigType type,
                         String signAlgName,
                         byte[] tbsContent,
                         byte[] signedValue)
            throws InvalidSignedValueException, IOException, GeneralSecurityException {
        if (type == SigType.Sign) {
            throw new IllegalArgumentException("签名类型(type)必须是 Seal，不支持电子seal/stamp验证");
        }

        // calculate original document digest
        MessageDigest md = new SM3.Digest();
        byte[] actualDataHash = md.digest(tbsContent);

        SES_Signature sesSignature = SES_Signature.getInstance(signedValue);
        TBS_Sign toSign = sesSignature.getToSign();
        byte[] expectDataHash = toSign.getDataHash().getOctets();

        // compare original document digest
        if (!Arrays.equals(actualDataHash, expectDataHash)) {
            throw new InvalidSignedValueException("Signature.xml 文件被篡改，电子seal/signature失效。("
                    + toSign.getPropertyInfo().getString() + ")");
        }

        // 验证seal/signature签名 Sign(TBS_Sign)
        byte[] expSigVal = sesSignature.getSignatureValue().getOctets();
        Signature sg = Signature.getInstance(sesSignature.getSignatureAlgID().getId(),
                new BouncyCastleProvider());
        byte[] certDER = sesSignature.getCert().getOctets();
        Certificate signCert = new CertificateFactory()
                .engineGenerateCertificate(new ByteArrayInputStream(certDER));
        sg.initVerify(signCert);
        sg.update(toSign.getEncoded("DER"));
        if (!sg.verify(expSigVal)) {
            throw new InvalidSignedValueException("电子seal/signature数据signature value不匹配，电子seal/signature数据失效。");
        }
    }
}
