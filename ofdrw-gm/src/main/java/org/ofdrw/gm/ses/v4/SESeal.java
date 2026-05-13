package org.ofdrw.gm.ses.v4;

import org.bouncycastle.asn1.*;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Enumeration;

/**
 * 电子seal/stamp数据
 *
 * @author Quan Guanyu
 * @since 2020-04-19 17:47:55
 */
public class SESeal extends ASN1Object {

    /**
     * seal information
     */
    private SES_SealInfo eSealInfo;

    /**
     * 制章人certificate
     */
    private ASN1OctetString cert;

    /**
     * signature algorithm identifier
     */
    private ASN1ObjectIdentifier signAlgID;

    /**
     * signature value
     */
    private ASN1BitString signedValue;

    public SESeal() {
        super();
    }

    public SESeal(SES_SealInfo eSealInfo,
                  ASN1OctetString cert,
                  ASN1ObjectIdentifier signAlgID,
                  ASN1BitString signedValue) {
        this.eSealInfo = eSealInfo;
        this.cert = cert;
        this.signAlgID = signAlgID;
        this.signedValue = signedValue;
    }

    public SESeal(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        eSealInfo = SES_SealInfo.getInstance(e.nextElement());
        cert = ASN1OctetString.getInstance(e.nextElement());
        signAlgID = ASN1ObjectIdentifier.getInstance(e.nextElement());
        signedValue = DERBitString.getInstance(e.nextElement());
    }


    public static SESeal getInstance(Object o) {
        if (o instanceof SESeal) {
            return (SESeal) o;
        } else if (o instanceof byte[]) {
            ASN1InputStream aIn = new ASN1InputStream((byte[]) o);
            try {
                final ASN1Primitive obj = aIn.readObject();
                return new SESeal(ASN1Sequence.getInstance(obj));
            } catch (IOException e) {
                throw new IllegalArgumentException("电子seal/stamp数据v4 无法解析", e);
            }
        } else if (o != null) {
            return new SESeal(ASN1Sequence.getInstance(o));
        }
        return null;
    }

    public SES_SealInfo geteSealInfo() {
        return eSealInfo;
    }

    public SESeal seteSealInfo(SES_SealInfo eSealInfo) {
        this.eSealInfo = eSealInfo;
        return this;
    }

    public ASN1OctetString getCert() {
        return cert;
    }

    public SESeal setCert(ASN1OctetString cert) {
        this.cert = cert;
        return this;
    }

    public SESeal setCert(Certificate cert) throws CertificateEncodingException {
        this.cert = new DEROctetString(cert.getEncoded());
        return this;
    }

    public ASN1ObjectIdentifier getSignAlgID() {
        return signAlgID;
    }

    public SESeal setSignAlgID(ASN1ObjectIdentifier signAlgID) {
        this.signAlgID = signAlgID;
        return this;
    }

    public ASN1BitString getSignedValue() {
        return signedValue;
    }

    public SESeal setSignedValue(ASN1BitString signedValue) {
        this.signedValue = signedValue;
        return this;
    }

    public SESeal setSignedValue(byte[] signedValue) {
        this.signedValue = new DERBitString(signedValue);
        return this;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(4);
        v.add(eSealInfo);
        v.add(cert);
        v.add(signAlgID);
        v.add(signedValue);
        return new DERSequence(v);
    }
}
