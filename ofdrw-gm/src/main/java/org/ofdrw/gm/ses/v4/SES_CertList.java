package org.ofdrw.gm.ses.v4;

import org.bouncycastle.asn1.*;

import java.io.IOException;

/**
 * signer certificate information list
 *
 * @author Quan Guanyu
 * @since 2020-04-19 17:19:36
 */
public class SES_CertList extends ASN1Object
        implements ASN1Choice {
    /**
     * signer certificate list
     */
    private final CertInfoList certs;

    /**
     * signer certificate hash value list
     */
    private final CertDigestList certDigestList;


    public SES_CertList(CertInfoList certs) {
        this.certs = certs;
        this.certDigestList = null;
    }

    public SES_CertList(CertDigestList certDigestList) {
        this.certs = null;
        this.certDigestList = certDigestList;
    }


    public static SES_CertList getInstance(ASN1Integer type, Object obj) {
        if (obj instanceof SES_CertList) {
            return (SES_CertList) obj;
        }

        if (obj != null) {
            if (obj instanceof ASN1Encodable) {
                final int t = type.getValue().intValue();
                if (t == 1) {
                    return new SES_CertList(CertInfoList.getInstance(obj));
                } else if (t == 2) {
                    return new SES_CertList(CertDigestList.getInstance(obj));
                } else {
                    throw new IllegalArgumentException("unknown type in getInstance(): " + obj.getClass().getName());
                }
            }
            if (obj instanceof byte[]) {
                try {
                    return getInstance(type, ASN1Primitive.fromByteArray((byte[]) obj));
                } catch (IOException e) {
                    throw new IllegalArgumentException("unknown encoding in getInstance()");
                }
            }
            throw new IllegalArgumentException("unknown object in getInstance(): " + obj.getClass().getName());
        }

        return null;
    }

    public ASN1Object get() {
        if (certs != null) {
            return certs;
        } else return certDigestList;
    }


    public CertInfoList getCerts() {
        return certs;
    }

    public CertDigestList getCertDigestList() {
        return certDigestList;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return get().toASN1Primitive();
    }
}
